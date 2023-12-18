package com.example.demo;

import io.javaoperatorsdk.operator.api.reconciler.ErrorStatusUpdateControl;

public class Utils {

    private Utils() {}

    public static WebPageStatus createStatus(String configMapName) {
        WebPageStatus status = new WebPageStatus();
        status.setHtmlConfigMap(configMapName);
        status.setAreWeGood(true);
        status.setErrorMessage(null);
        return status;
    }

    public static String configMapName(WebPage nginx) {
        return nginx.getMetadata().getName() + "-html";
    }

    public static String deploymentName(WebPage nginx) {
        return nginx.getMetadata().getName();
    }

    public static String serviceName(WebPage webPage) {
        return webPage.getMetadata().getName();
    }

    public static ErrorStatusUpdateControl<WebPage> handleError(WebPage resource, Exception e) {
        resource.getStatus().setErrorMessage("Error: " + e.getMessage());
        return ErrorStatusUpdateControl.updateStatus(resource);
    }

    public static boolean isValidHtml(WebPage webPage) {
        // very dummy html validation
        var lowerCaseHtml = webPage.getSpec().getHtml().toLowerCase();
        return lowerCaseHtml.contains("<html>") && lowerCaseHtml.contains("</html>");
    }

    public static WebPage setInvalidHtmlErrorMessage(WebPage webPage) {
        if (webPage.getStatus() == null) {
            webPage.setStatus(new WebPageStatus());
        }
        webPage.getStatus().setErrorMessage("Invalid html.");
        return webPage;
    }
}
