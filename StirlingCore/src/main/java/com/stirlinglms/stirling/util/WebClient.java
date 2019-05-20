package com.stirlinglms.stirling.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;

import javax.annotation.Nonnull;

public final class WebClient extends com.gargoylesoftware.htmlunit.WebClient {

    public WebClient(@Nonnull DefaultCredentialsProvider provider) {
        super(BrowserVersion.CHROME);

        this.setCredentialsProvider(provider);
        this.setAjaxController(new NicelyResynchronizingAjaxController());

        this.getOptions().setJavaScriptEnabled(true);
        this.getOptions().setUseInsecureSSL(true);
        this.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.getOptions().setThrowExceptionOnScriptError(false);
        this.getOptions().setCssEnabled(false);

    }
}
