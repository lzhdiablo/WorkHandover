/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.dingjust.platform.klotho.storefront.setup;

import static com.dingjust.platform.klotho.storefront.constants.DjklothostorefrontConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.dingjust.platform.klotho.storefront.constants.DjklothostorefrontConstants;
import com.dingjust.platform.klotho.storefront.service.DjklothostorefrontService;


@SystemSetup(extension = DjklothostorefrontConstants.EXTENSIONNAME)
public class DjklothostorefrontSystemSetup
{
	private final DjklothostorefrontService djklothostorefrontService;

	public DjklothostorefrontSystemSetup(final DjklothostorefrontService djklothostorefrontService)
	{
		this.djklothostorefrontService = djklothostorefrontService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		djklothostorefrontService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return DjklothostorefrontSystemSetup.class.getResourceAsStream("/djklothostorefront/sap-hybris-platform.png");
	}
}
