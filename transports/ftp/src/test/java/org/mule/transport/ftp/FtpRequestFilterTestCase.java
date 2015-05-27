/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.transport.ftp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.mule.api.MuleMessage;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runners.Parameterized;

public class FtpRequestFilterTestCase extends AbstractFtpServerTestCase
{

    private static final String FILE_TXT = "file.txt";
    private static final String FILE_XML = "file.xml";
    private static final String FTP_FILTER_ENDPOINT = "ftpFilterEndpoint";

    public FtpRequestFilterTestCase(ConfigVariant variant, String configResources)
    {
        super(variant, configResources);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters()
    {
        return Arrays.asList(new Object[][] {
                {ConfigVariant.FLOW, "ftp-request-filter-config.xml"}
        });
    }

    @Test
    public void filtersFile() throws Exception
    {
        createFileOnFtpServer(FILE_TXT);

        MuleMessage response = muleContext.getClient().request(FTP_FILTER_ENDPOINT, RECEIVE_TIMEOUT);

        assertNotNull("The file was not processed.", response);
        assertTrue(TEST_MESSAGE.equals(response.getPayloadAsString()));
        assertFalse(fileExists(FILE_TXT));
    }

    @Test
    public void rejectsFile() throws Exception
    {
        createFileOnFtpServer(FILE_XML);

        MuleMessage response = muleContext.getClient().request(FTP_FILTER_ENDPOINT, RECEIVE_TIMEOUT);

        assertNull("The file was processed.", response);
        assertTrue(fileExists(FILE_XML));
    }

}
