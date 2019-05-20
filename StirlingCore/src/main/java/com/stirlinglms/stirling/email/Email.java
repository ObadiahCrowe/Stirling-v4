package com.stirlinglms.stirling.email;

import com.google.common.collect.Sets;
import com.stirlinglms.stirling.exception.EmailNoContentException;
import com.stirlinglms.stirling.exception.EmailNoRecipientException;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;
import java.util.Set;

@Getter
@Immutable
public final class Email {

    private final String subject;
    private final Set<String> to;
    private final Set<String> cc;
    private final Set<String> bcc;
    private final String content;

    private Email(String subject, Set<String> to, Set<String> cc, Set<String> bcc, String content) {
        this.subject = subject;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.content = content;
    }

    public static class Builder {

        private final String subject;

        private final Set<String> to;
        private final Set<String> cc;
        private final Set<String> bcc;

        private String content;

        public Builder(String subject) {
            this.subject = subject;

            this.to = Sets.newConcurrentHashSet();
            this.cc = Sets.newConcurrentHashSet();
            this.bcc = Sets.newConcurrentHashSet();

            this.content = "";
        }

        public Builder addTo(String to) {
            this.to.add(to);

            return this;
        }

        public Builder addCc(String cc) {
            this.cc.add(cc);

            return this;
        }

        public Builder addBcc(String bcc) {
            this.bcc.add(bcc);

            return this;
        }

        public Builder setContent(String content) {
            this.content = content;

            return this;
        }

        public Email build() throws EmailNoContentException, EmailNoRecipientException {
            if (this.to.size() < 1) {
                throw new EmailNoRecipientException("You have not specified a recipient of this email!");
            }

            if (this.content.equalsIgnoreCase("")) {
                throw new EmailNoContentException("You have not provided any content in this email!");
            }

            return new Email(this.subject, this.to, this.cc, this.bcc, this.content);
        }
    }
}
