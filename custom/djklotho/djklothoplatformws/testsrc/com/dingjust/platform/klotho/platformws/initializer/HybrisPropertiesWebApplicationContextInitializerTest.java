/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.dingjust.platform.klotho.platformws.initializer;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dingjust.platform.klotho.platformws.constants.DjklothoplatformwsConstants;


@IntegrationTest
@NeedsEmbeddedServer(webExtensions =
{ DjklothoplatformwsConstants.EXTENSIONNAME, "oauth2" })
public class HybrisPropertiesWebApplicationContextInitializerTest extends ServicelayerTest
{
	public static final String OAUTH_CLIENT_ID = "mobile_android";
	public static final String OAUTH_CLIENT_PASS = "secret";

	private WsSecuredRequestBuilder wsSecuredRequestBuilder;

	@Before
	public void setUp() throws Exception
	{
		wsSecuredRequestBuilder = new WsSecuredRequestBuilder()//
				.extensionName(DjklothoplatformwsConstants.EXTENSIONNAME)//
				.client(OAUTH_CLIENT_ID, OAUTH_CLIENT_PASS);

		createCoreData();
		importCsv("/djklothoplatformws/test/democustomer-data.impex", "utf-8");
	}

	@Test
	public void testSpringFileOverride() throws IOException
	{
		final Response response = wsSecuredRequestBuilder.grantClientCredentials().path("/sample/testBean").build()
				.accept(MediaType.APPLICATION_JSON).get();

		Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_OK);

		final TestBean testBean = response.readEntity(TestBean.class);
		Assert.assertEquals("testText", testBean.getText());
	}
}
