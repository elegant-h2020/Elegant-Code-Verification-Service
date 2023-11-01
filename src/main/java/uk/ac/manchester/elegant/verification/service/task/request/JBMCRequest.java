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

/**
 * This class represents a code verification request with the JBMC tool.
 *
 */
public class JBMCRequest implements Request{

    private String className;
    private String relativeClassPath;
    private boolean isMethod;
    // Fully qualified name of a method.
    private String methodName;

    public JBMCRequest(String className, boolean isMethod, String methodName) {
        this.className = className;
        this.isMethod = isMethod;
        this.methodName = methodName;
        this.relativeClassPath = resolveRelativeClassPath(className);
    }

    private String resolveRelativeClassPath(String className) {
        String classNameWithFileSeparator = className.replace(".", "/");
        return classNameWithFileSeparator.substring(0, classNameWithFileSeparator.lastIndexOf("/"));
    }

    public static JBMCRequest asJBMCRequest(Request request) {
        return (JBMCRequest) request;
    }

    public String getClassName() {
        return className;
    }

    public boolean isMethod() {
        return isMethod;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getRelativeClassPath() { return relativeClassPath; }

    public void setClassName(String classname) {
        this.className = classname;
    }

    public void setIsMethod(boolean isMethod) {
        this.isMethod = isMethod;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setRelativeClassPath(String relativeClassPath) {
        this.relativeClassPath = relativeClassPath;
    }

    @Override
    public String toString() {
        return "JBMCRequest{" +
                ", className = \""    + className     + "\"" +
                ", isMethod = "       + isMethod      +
                ", methodName = \""   + methodName    + "\"" +
                "}";
    }
}
