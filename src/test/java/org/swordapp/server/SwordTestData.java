package org.swordapp.server;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.swordapp.client.AuthCredentials;

public class SwordTestData {
    public static final int TEST_PORT = 8292;

    public static final String TEST_PORT_STRING = String.valueOf(TEST_PORT);

    public static final String BASE_URL = "http://localhost:" + TEST_PORT_STRING;

    public static final String SD_URI = BASE_URL + "/sword/sd";

    public static final String TEST_PACKAGING = "Sword/TestPackaging";

    public static final String TEST_COLLECTION_HREF = BASE_URL + "/sword/col/testCollection";

    public static final String TEST_COLLECTION_TITLE = "Test-Title";

    public static final String TEST_WORKSPACE_TITLE = "Test-Workspace";

    public static final String TEST_DATA_FILE_NAME = "lorem_ipsum.txt";

    public static final String TEST_DEPOSIT_LOCATION = TEST_COLLECTION_HREF + "/" + TEST_DATA_FILE_NAME;

    public static final ServiceDocument SD;

    public static final SwordWorkspace WS;

    public static final SwordCollection COL;

    public static final Feed COL_LIST;

    public static final int MAX_UPLOAD_SIZE = 1024 * 1024 * 10;

    public static final String AUTH_TYPE = "Basic";

    public static final String ADMIN_TEST_MAIL = "AdminTESTMail@sword.com";

    public static final String GENERATOR_VERSION = "TEST-1";

    public static final String GENERATOR_NAME = "Generator-1";

    public static final org.swordapp.client.AuthCredentials TEST_AUTH = new AuthCredentials("admin", "alleswirdgut");

    public static final String TEST_TITLE_1 = "TEST_TITLE_1";

    public static final String TEST_SUMMARY_1 = "TEST_SUMMARY_1";

    public static final String TEST_TITLE_2 = "TEST_TITLE_2";

    public static final String TEST_SUMMARY_2 = "TEST_SUMMARY_2";

    public static final String TEST_DATA = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy "
        + "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam "
        + "et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor "
        + "sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt "
        + "ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et "
        + "ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";



    static {
        SD = new ServiceDocument();
        WS = new SwordWorkspace();
        COL = new SwordCollection();

        COL.addAcceptPackaging(TEST_PACKAGING);
        COL.setHref(TEST_COLLECTION_HREF);
        COL.setTitle(TEST_COLLECTION_TITLE);
        WS.addCollection(COL);
        WS.setTitle(TEST_WORKSPACE_TITLE);

        SD.addWorkspace(WS);

        final Abdera abdera = new Abdera();
        COL_LIST = abdera.newFeed();

        final Entry entry1 = abdera.newEntry();
        entry1.setTitle(TEST_TITLE_1);
        entry1.setSummary(TEST_SUMMARY_1);
        COL_LIST.addEntry(entry1);

        final Entry entry2 = abdera.newEntry();
        entry2.setTitle(TEST_TITLE_2);
        entry2.setSummary(TEST_SUMMARY_2);
        COL_LIST.addEntry(entry2);
    }

}
