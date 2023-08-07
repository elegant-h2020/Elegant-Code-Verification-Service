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

package uk.ac.manchester.elegant.verification.service.task.request;

public class ESBMCRequest implements Request{

    private String fileName;
    private boolean isFunction;
    private String functionName;

    public ESBMCRequest(String fileName, boolean isFunction, String functionName) {
        this.fileName = fileName;
        this.isFunction = isFunction;
        this.functionName = functionName;
    }

    public static ESBMCRequest asESBMCRequest(Request request) {
        return (ESBMCRequest) request;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setIsFunction(boolean isMethod) {
        this.isFunction = isMethod;
    }

    public void setFunction(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "ESBMCRequest{"          +
                ", fileName = \""       + fileName      + "\"" +
                ", isFunction = "       + isFunction    +
                ", functionName = \""   + functionName  + "\"" +
                "}";
    }
}
