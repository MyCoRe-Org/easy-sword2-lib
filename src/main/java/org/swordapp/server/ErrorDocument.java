package org.swordapp.server;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ErrorDocument {

    private String errorUri = null;
    private Map<String, Integer> errorCodes = new HashMap<String, Integer>();
    private String summary = null;
    private String verboseDescription = null;
    private int status;

    public static final Namespace SWORD_NS = Namespace.getNamespace(UriRegistry.SWORD_PREFIX,
        UriRegistry.SWORD_TERMS_NAMESPACE);

    public static final Namespace ATOM_NS = Namespace.getNamespace(UriRegistry.ATOM_PREFIX, UriRegistry.ATOM_NAMESPACE);

    public ErrorDocument(String errorUri) {
        this(errorUri, -1, null, null);
    }

    public ErrorDocument(String errorUri, int status) {
        this(errorUri, status, null, null);
    }

    public ErrorDocument(String errorUri, String verboseDescription) {
        this(errorUri, -1, null, verboseDescription);
    }

    public ErrorDocument(String errorUri, int status, String verboseDescription) {
        this(errorUri, status, null, verboseDescription);
    }

    public ErrorDocument(String errorUri, int status, String summary, String verboseDescription) {
        // set up the error codes mapping
        this.errorCodes.put(UriRegistry.ERROR_BAD_REQUEST, 400); // bad request
        this.errorCodes.put(UriRegistry.ERROR_CHECKSUM_MISMATCH, 412); // precondition failed
        this.errorCodes.put(UriRegistry.ERROR_CONTENT, 415); // unsupported media type
        this.errorCodes.put(UriRegistry.ERROR_MEDIATION_NOT_ALLOWED, 412); // precondition failed
        this.errorCodes.put(UriRegistry.ERROR_METHOD_NOT_ALLOWED, 405); // method not allowed
        this.errorCodes.put(UriRegistry.ERROR_TARGET_OWNER_UNKNOWN, 403); // forbidden
        this.errorCodes.put(UriRegistry.ERROR_MAX_UPLOAD_SIZE_EXCEEDED, 413); // forbidden

        this.errorUri = errorUri;
        this.summary = summary;
        this.verboseDescription = verboseDescription;
        this.status = status;
    }

    public int getStatus() {
        if (this.status > -1) {
            return this.status;
        }

        if (errorUri != null && this.errorCodes.containsKey(errorUri)) {
            return this.errorCodes.get(errorUri);
        } else {
            return 400; // bad request
        }
    }

    public void writeTo(Writer out, SwordConfiguration config) throws IOException {
        // do the XML serialisation
        Element error = new Element("error", SWORD_NS);
        error.setAttribute(new Attribute("href", this.errorUri));

        // write some boiler-plate text into the document
        Element title = new Element("title", ATOM_NS);
        title.setText("ERROR");
        Element updates = new Element("updated", ATOM_NS);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        updates.setText(sdf.format(new Date()));
        Element generator = new Element("generator", ATOM_NS);
        generator.setAttribute(new Attribute("uri", config.generator()));
        generator.setAttribute(new Attribute("version", config.generatorVersion()));
        if (config.administratorEmail() != null) {
            generator.setText(config.administratorEmail());
        }
        Element treatment = new Element("treatment", SWORD_NS);
        treatment.setText("Processing failed");

        error.addContent(title);
        error.addContent(updates);
        error.addContent(generator);
        error.addContent(treatment);

        // now add the operational bits
        if (this.summary != null) {
            Element summary = new Element("summary", ATOM_NS);
            summary.setText(this.summary);
            error.addContent(summary);
        }

        if (this.verboseDescription != null) {
            Element vd = new Element("verboseDescription", SWORD_NS);
            vd.setText(this.verboseDescription);
            error.addContent(vd);
        }

        String alternate = config.getAlternateUrl();
        String altContentType = config.getAlternateUrlContentType();
        if (alternate != null && !"".equals(alternate)) {
            Element altLink = new Element("link", ATOM_NS);
            altLink.setAttribute(new Attribute("rel", "alternate"));
            if (altContentType != null && !"".equals(altContentType)) {
                altLink.setAttribute(new Attribute("type", altContentType));
            }
            altLink.setAttribute(new Attribute("href", alternate));
            error.addContent(altLink);
        }

        // now get it written out
        Document doc = new Document(error);
        Format format = Format.getCompactFormat();
        format.setEncoding(StandardCharsets.ISO_8859_1.name());
        XMLOutputter xout = new XMLOutputter(format);
        xout.output(doc, out);
    }
}
