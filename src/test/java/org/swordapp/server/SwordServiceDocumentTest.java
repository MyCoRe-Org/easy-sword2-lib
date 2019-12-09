package org.swordapp.server;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.swordapp.client.ProtocolViolationException;
import org.swordapp.client.SWORDClient;
import org.swordapp.client.SWORDClientException;
import org.swordapp.client.SWORDCollection;
import org.swordapp.client.SWORDWorkspace;

public class SwordServiceDocumentTest extends SwordTest {

    @Before
    public void before() throws Exception {
        SwordTestImpl.setServiceDocumentManager(new ServiceDocumentManager() {
            @Override
            public ServiceDocument getServiceDocument(String sdUri, AuthCredentials auth, SwordConfiguration config)
                throws SwordError, SwordServerException, SwordAuthException {
                return SwordTestData.SD;
            }
        });
        startJetty();
    }

    @Test
    public void testServiceDocument() throws SWORDClientException, ProtocolViolationException {
        SWORDClient sc = new SWORDClient();
        final org.swordapp.client.ServiceDocument serviceDocument = sc.getServiceDocument(SwordTestData.SD_URI,
            SwordTestData.TEST_AUTH);

        Assert.assertNotNull("Service Document should be not null!",serviceDocument);
        Assert.assertEquals("There should be exact one workspace", 1, serviceDocument.getWorkspaces().size());
        final SWORDWorkspace workspace = serviceDocument.getWorkspace(SwordTestData.TEST_WORKSPACE_TITLE);
        Assert.assertNotNull("The workspace should be receivable by its title!", workspace);
        Assert
            .assertEquals("The titles should match exactly", SwordTestData.TEST_WORKSPACE_TITLE, workspace.getTitle());

        Assert.assertEquals("There should be exact one Collection", 1, workspace.getCollections().size());
        final SWORDCollection collection = workspace.getCollection(SwordTestData.TEST_COLLECTION_TITLE);
        Assert.assertNotNull("The collection should be receivable by its title!", collection);
        Assert.assertEquals("The titles should match exactly", SwordTestData.TEST_COLLECTION_TITLE,
            collection.getTitle());

        Assert.assertTrue("Collection supports packaging!",
            collection.getAcceptPackaging().contains(SwordTestData.TEST_PACKAGING));
    }

    @After
    public void killServer() throws Exception {
        stopJetty();
    }


}
