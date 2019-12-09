package org.swordapp.server;

import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Feed;

public class SwordTestImpl extends HttpServlet implements
    CollectionListManager,
    CollectionDepositManager,
    ContainerManager,
    MediaResourceManager,
    ServiceDocumentManager,
    StatementManager,
    SwordConfiguration {

    public SwordTestImpl(){
    }

    private static CollectionListManager listManager;

    private static CollectionDepositManager depositManager;

    private static ContainerManager containerManager;

    private static MediaResourceManager mediaResourceManager;

    private static ServiceDocumentManager serviceDocumentManager;

    private static StatementManager statementManager;

    public static CollectionListManager getListManager() {
        return listManager;
    }

    public static void setListManager(CollectionListManager listManager) {
        SwordTestImpl.listManager = listManager;
    }

    public static CollectionDepositManager getDepositManager() {
        return depositManager;
    }

    public static void setDepositManager(CollectionDepositManager depositManager) {
        SwordTestImpl.depositManager = depositManager;
    }

    public static ContainerManager getContainerManager() {
        return containerManager;
    }

    public static void setContainerManager(ContainerManager containerManager) {
        SwordTestImpl.containerManager = containerManager;
    }

    public static MediaResourceManager getMediaResourceManager() {
        return mediaResourceManager;
    }

    public void setMediaResourceManager(MediaResourceManager mediaResourceManager) {
        SwordTestImpl.mediaResourceManager = mediaResourceManager;
    }

    public static ServiceDocumentManager getServiceDocumentManager() {
        return serviceDocumentManager;
    }

    public static void setServiceDocumentManager(ServiceDocumentManager serviceDocumentManager) {
        SwordTestImpl.serviceDocumentManager = serviceDocumentManager;
    }

    public static StatementManager getStatementManager() {
        return statementManager;
    }

    public static void setStatementManager(StatementManager statementManager) {
        SwordTestImpl.statementManager = statementManager;
    }

    @Override
    public DepositReceipt createNew(String collectionURI, Deposit deposit, AuthCredentials auth,
        SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {
        return depositManager.createNew(collectionURI, deposit, auth, config);
    }

    @Override
    public Feed listCollectionContents(IRI collectionIRI, AuthCredentials auth, SwordConfiguration config)
        throws SwordServerException, SwordAuthException, SwordError {
        return listManager.listCollectionContents(collectionIRI, auth, config);
    }

    @Override
    public DepositReceipt getEntry(String editIRI, Map<String, String> accept, AuthCredentials auth,
        SwordConfiguration config) throws SwordServerException, SwordError, SwordAuthException {
        return containerManager.getEntry(editIRI, accept, auth, config);
    }

    @Override
    public DepositReceipt replaceMetadata(String editIRI, Deposit deposit, AuthCredentials auth,
        SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        return containerManager.replaceMetadata(editIRI, deposit, auth, config);
    }

    @Override
    public DepositReceipt replaceMetadataAndMediaResource(String editIRI, Deposit deposit, AuthCredentials auth,
        SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        return containerManager.replaceMetadataAndMediaResource(editIRI, deposit, auth, config);
    }

    @Override
    public DepositReceipt addMetadataAndResources(String editIRI, Deposit deposit, AuthCredentials auth,
        SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        return containerManager.addMetadataAndResources(editIRI, deposit, auth, config);
    }

    @Override
    public DepositReceipt addMetadata(String editIRI, Deposit deposit, AuthCredentials auth, SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {
        return containerManager.addMetadata(editIRI, deposit, auth, config);
    }

    @Override
    public DepositReceipt addResources(String editIRI, Deposit deposit, AuthCredentials auth, SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {
        return containerManager.addResources(editIRI, deposit, auth, config);
    }

    @Override public void deleteContainer(String editIRI, AuthCredentials auth, SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {
        containerManager.deleteContainer(editIRI, auth, config);
    }

    @Override
    public DepositReceipt useHeaders(String editIRI, Deposit deposit, AuthCredentials auth, SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {
        return containerManager.useHeaders(editIRI, deposit, auth, config);
    }

    @Override
    public boolean isStatementRequest(String editIRI, Map<String, String> accept, AuthCredentials auth,
        SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        return containerManager.isStatementRequest(editIRI, accept, auth, config);
    }

    @Override
    public MediaResource getMediaResourceRepresentation(String uri, Map<String, String> accept, AuthCredentials auth,
        SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        return mediaResourceManager.getMediaResourceRepresentation(uri, accept, auth, config);
    }

    @Override
    public DepositReceipt replaceMediaResource(String uri, Deposit deposit, AuthCredentials auth,
        SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {
        return mediaResourceManager.replaceMediaResource(uri, deposit, auth, config);
    }

    @Override public void deleteMediaResource(String uri, AuthCredentials auth, SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {
        mediaResourceManager.deleteMediaResource(uri, auth, config);
    }

    @Override
    public DepositReceipt addResource(String uri, Deposit deposit, AuthCredentials auth, SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {
        return mediaResourceManager.addResource(uri, deposit, auth, config);
    }

    @Override
    public ServiceDocument getServiceDocument(String sdUri, AuthCredentials auth, SwordConfiguration config)
        throws SwordError, SwordServerException, SwordAuthException {

        return serviceDocumentManager.getServiceDocument(sdUri, auth, config);
    }

    @Override
    public Statement getStatement(String iri, Map<String, String> accept, AuthCredentials auth,
        SwordConfiguration config) throws SwordServerException, SwordError, SwordAuthException {
        return statementManager.getStatement(iri, accept, auth, config);
    }

    @Override
    public boolean returnDepositReceipt() {
        return true;
    }

    @Override
    public boolean returnStackTraceInError() {
        return true;
    }

    @Override
    public boolean returnErrorBody() {
        return true;
    }

    @Override
    public String generator() {
        return SwordTestData.GENERATOR_NAME;
    }

    @Override
    public String generatorVersion() {
        return SwordTestData.GENERATOR_VERSION;
    }

    @Override public String administratorEmail() {
        return SwordTestData.ADMIN_TEST_MAIL;
    }

    @Override
    public String getAuthType() {
        return SwordTestData.AUTH_TYPE;
    }

    @Override
    public boolean storeAndCheckBinary() {
        return true;
    }

    @Override
    public String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    @Override
    public long getMaxUploadSize() {
        return SwordTestData.MAX_UPLOAD_SIZE;
    }

    @Override
    public String getAlternateUrl() {
        return null;
    }

    @Override
    public String getAlternateUrlContentType() {
        return null;
    }

    @Override
    public boolean allowUnauthenticatedMediaAccess() {
        return false;
    }
}
