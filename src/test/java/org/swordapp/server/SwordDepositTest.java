package org.swordapp.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Feed;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.swordapp.client.SWORDClient;

import static org.swordapp.server.SwordTestData.TEST_DATA;

public class SwordDepositTest extends SwordTest {

    AtomicBoolean depositReceivedAndOk;

    AtomicReference<String> receivedFileContent;

    AtomicReference<String> receivedFileName;

    @Before
    public void before() throws Exception {
        SwordTestImpl.setServiceDocumentManager(new ServiceDocumentManager() {
            @Override
            public ServiceDocument getServiceDocument(String sdUri, AuthCredentials auth, SwordConfiguration config)
                throws SwordError, SwordServerException, SwordAuthException {
                return SwordTestData.SD;
            }
        });

        SwordTestImpl.setListManager(new CollectionListManager() {
            @Override
            public Feed listCollectionContents(IRI collectionIRI, AuthCredentials auth, SwordConfiguration config)
                throws SwordServerException, SwordAuthException, SwordError {
                return SwordTestData.COL_LIST;
            }
        });
        SwordTestImpl.setDepositManager(new CollectionDepositManager() {
            @Override
            public DepositReceipt createNew(String collectionURI, Deposit deposit, AuthCredentials auth,
                SwordConfiguration config) throws SwordError, SwordServerException, SwordAuthException {

                receivedFileName.set(deposit.getFilename());
                try (BufferedReader is = new BufferedReader(new InputStreamReader(deposit.getInputStream()))) {
                    final String result = is.lines().collect(Collectors.joining("\n"));
                    receivedFileContent.set(result);
                } catch (IOException e) {
                    return null;
                }

                final DepositReceipt receipt = new DepositReceipt();

                receipt.getWrappedEntry().setTitle(SwordTestData.TEST_TITLE_1);
                receipt.getWrappedEntry().setSummary(SwordTestData.TEST_SUMMARY_1);
                try {
                    receipt.setLocation(new IRI(new URI(SwordTestData.TEST_DEPOSIT_LOCATION)));
                    receipt.setEditIRI(new IRI(new URI(SwordTestData.TEST_DEPOSIT_LOCATION + "?edit")));
                } catch (URISyntaxException e) {
                    return null;
                }

                depositReceivedAndOk.set(true);

                return receipt;
            }
        });

        startJetty();
        depositReceivedAndOk = new AtomicBoolean(false);
        receivedFileContent = new AtomicReference<>();
        receivedFileName = new AtomicReference<>();
    }

    @Test
    public void testDepositToCollection() throws Exception {
        SWORDClient sc = new SWORDClient();
        final org.swordapp.client.ServiceDocument serviceDocument = sc.getServiceDocument(SwordTestData.SD_URI,
            SwordTestData.TEST_AUTH);

        final org.swordapp.client.Deposit deposit = new org.swordapp.client.Deposit();
        final org.swordapp.client.DepositReceipt depositResult;

        try (InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8))) {
            deposit.setFile(is);
            deposit.setFilename(SwordTestData.TEST_DATA_FILE_NAME);
            depositResult = sc.deposit(
                serviceDocument.getWorkspace(SwordTestData.TEST_WORKSPACE_TITLE)
                    .getCollection(SwordTestData.TEST_COLLECTION_TITLE), deposit, SwordTestData.TEST_AUTH);
        }

        Assert.assertTrue("The content should have been received by the server!", depositReceivedAndOk.get());
        Assert.assertEquals("The received filename should match", SwordTestData.TEST_DATA_FILE_NAME,
            receivedFileName.get());
        Assert.assertEquals("The received file content should match", TEST_DATA,
            receivedFileContent.get());

        Assert.assertEquals("The title of the deposit should match!", SwordTestData.TEST_TITLE_1,
            depositResult.getEntry().getTitle());
        Assert.assertEquals("The summary of the deposit should match!", SwordTestData.TEST_SUMMARY_1,
            depositResult.getEntry().getSummary());

        Assert.assertEquals("The location should match!", SwordTestData.TEST_DEPOSIT_LOCATION,
            depositResult.getLocation());
    }

    @After
    public void killServer() throws Exception {
        stopJetty();
    }
}
