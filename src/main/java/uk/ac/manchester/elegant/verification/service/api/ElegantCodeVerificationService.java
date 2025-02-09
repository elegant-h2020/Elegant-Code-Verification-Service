/*
 * This file is part of the ELEGANT Code Verification Service.
 * URL: https://github.com/elegant-h2020/Elegant-Code-Verification-Service
 *
 * Copyright (c) 2022-2023, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.manchester.elegant.verification.service.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import uk.ac.manchester.elegant.verification.service.task.request.ESBMCRequest;
import uk.ac.manchester.elegant.verification.service.task.request.FileHandler;
import uk.ac.manchester.elegant.verification.service.task.request.JBMCRequest;
import uk.ac.manchester.elegant.verification.service.task.request.Request;
import uk.ac.manchester.elegant.verification.service.task.VerificationTasks;
import uk.ac.manchester.elegant.verification.service.task.ServiceThreadPoolExecutor;
import uk.ac.manchester.elegant.verification.service.task.VerificationTask;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Path("/verification")
public class ElegantCodeVerificationService {

    /**
     * The unique id of the current execution of the service.
     */
    private static String serviceUId;

    /**
     * The unique id of a Task.
     */
    private static final AtomicLong uid;
    /**
     * A blocking queue that stores the incoming requests.
     */
    private static BlockingQueue<Runnable> blockingQueue;
    /**
     * A customized {@link ThreadPoolExecutor} that consumes the incoming requests in a thread-safe manner.
     */
    private static ServiceThreadPoolExecutor executor;
    /**
     * A shared data structure that stores all the submitted {@link VerificationTask}s.
     */
    private static VerificationTasks verificationTasks;

    // service initialization
    static {
        setExecutionUId();
        verificationTasks = new VerificationTasks();
        uid = new AtomicLong(-1);
        blockingQueue = new LinkedBlockingQueue<Runnable>();
        executor = new ServiceThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, blockingQueue);

        // Start all core threads of the executor
        executor.prestartAllCoreThreads();
    }

    /**
     * Set the unique id of the current execution of the service in the yyyyMMdd-HHmm format.
     */
    public static void setExecutionUId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
        serviceUId = now.format(formatter);
    }

    public static String getServiceUId() {
        return serviceUId;
    }

    public static VerificationTasks getVerificationTasks() {
        return verificationTasks;
    }

    /**
     * Starting Point of the Code Verification Service.
     */
    @GET
    @Produces("text/plain")
    public Response serviceStart() {

        return Response
                .status(Response.Status.OK)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("Code Verification Service : START!\n")
                .build();
    }

    /**
     * Handles the incoming JBMC or ESBMC request.
     * @param request a code verification {@link Request} object.
     * @param tool the {@link String} name of the code verification tool.
     * @return the {@link Response} of the service.
     */
    public Response handle(Request request, String tool) {
        // increment unique id
        final long entryId = uid.incrementAndGet();
        // wrap the request as a VerificationTask and add it into the queue
        final boolean accepted = blockingQueue.offer(new VerificationTask(entryId, tool, request));
        if (!accepted) {
            // no space is currently available for the task
            return Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Overloaded! The service currently unable to accept more requests.")
                    .build();
        }

        return Response.status(Response.Status.ACCEPTED)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("New code verification request has been registered (#" + entryId + ")\n")
                .build();
    }

    @POST
    @Path("newEntry")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response submit(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileMetaData,
                           @FormDataParam("request") InputStream requestInputStream) {
        Request request = FileHandler.receiveRequest(requestInputStream);
        if (!FileHandler.receiveFile(fileInputStream, fileMetaData, request)) {
            return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Input file error!")
                    .build();
        }

        if (request instanceof JBMCRequest) {
            return handle(request, "JBMC");
        } else if (request instanceof ESBMCRequest) {
            return handle(request, "ESBMC");
        } else {
            return Response
                    .status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Not acceptable request.")
                    .build();
        }

    }

    /**
     * Get the status of an entry.
     */
    @GET
    @Path("getStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(@QueryParam("entryId") String entryId) {
        int id = Integer.parseInt(entryId);
        VerificationTask task = verificationTasks.getTask(id);
        if (task != null) {
            return Response
                    .status(Response.Status.ACCEPTED)
                    .entity(task.getStatus())
                    .build();
        } else {
            return Response
                    .status(Response.Status.ACCEPTED)
                    .entity("Invalid Entry!")
                    .build();
        }
    }

    /**
     * Get the verification outcome of an entry.
     */
    @GET
    @Path("getEntry")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntry(@QueryParam("entryId") String entryId) {

        int id = Integer.parseInt(entryId);
        VerificationTask task = verificationTasks.getTask(id);

        if (task != null) {
            File file = FileHandler.writeObjectToJsonFile(task, id);

            return Response
                    .ok(file, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Invalid Entry!!")
                    .build();
        }
    }

    /**
     * Remove an entry.
     */
    @DELETE
    @Path("removeEntry")
    @Produces("text/plain")
    public Response removeEntry(@QueryParam("entryId") long entryId) {
        VerificationTask task = verificationTasks.getTask(entryId);
        if (task != null) {
            VerificationTask deleted = verificationTasks.removeEntry(entryId);
            Response.Status responseStatus = (deleted != null) ? Response.Status.FOUND : Response.Status.NOT_FOUND;
            String responseMsg = (deleted != null) ? "Code verification entry (#" + entryId + ") has been deleted.\n" : "Invalid Entry.\n";
            return Response.status(responseStatus).type(MediaType.TEXT_PLAIN_TYPE).entity(responseMsg).build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Invalid Entry!!")
                    .build();
        }
    }

    /**
     * List all known verification entries.
     */
    @GET
    @Path("getEntries")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntries() {

        return Response
                .status(Response.Status.ACCEPTED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(verificationTasks.listEntries())
                .build();
    }
}