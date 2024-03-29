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
package com.dingjust.platform.klotho.platformws.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import com.dingjust.platform.klotho.platformws.constants.DjklothoplatformwsConstants;

public class DjklothoplatformwsManager extends GeneratedDjklothoplatformwsManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( DjklothoplatformwsManager.class.getName() );
	
	public static final DjklothoplatformwsManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (DjklothoplatformwsManager) em.getExtension(DjklothoplatformwsConstants.EXTENSIONNAME);
	}
	
}
