package org.swordapp.server;

import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Feed;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.swordapp.client.CollectionEntries;
import org.swordapp.client.SWORDClient;

public class SwordCollectionTest extends SwordTest {

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


        startJetty();
    }

    @Test
    public void testCollection() throws Exception {
        SWORDClient sc = new SWORDClient();
        final CollectionEntries collectionEntries = sc
            .listCollection(SwordTestData.TEST_COLLECTION_HREF, SwordTestData.TEST_AUTH);

        Assert.assertNotNull("The collection entry object should not be null", collectionEntries);

        Assert.assertEquals("The two test entries should be present", 2,
            collectionEntries.getEntries().stream()
                .peek(Assert::assertNotNull)
                .filter(entry ->
                SwordTestData.TEST_TITLE_1.equals(entry.getTitle()) && SwordTestData.TEST_SUMMARY_1
                    .equals(entry.getSummary())
                    || SwordTestData.TEST_TITLE_2.equals(entry.getTitle()) && SwordTestData.TEST_SUMMARY_2
                    .equals(entry.getSummary())).count());

    }

    @After
    public void killServer() throws Exception {
        stopJetty();
    }
}
