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

package uk.ac.manchester.elegant.verification.service.tool;

import uk.ac.manchester.elegant.verification.service.task.request.Request;

import java.io.IOException;

public interface VerificationTool {

    void setUpToolEnvironment() throws IOException, InterruptedException;
    void verifyCode(Request code) throws IOException, InterruptedException;
    Object readOutput();
    String getEnvironmentVariable(String var);
    String getName();
    int getExitCode();
    String getOutput();
    int waitFor() throws InterruptedException;
}
