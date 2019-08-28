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
package com.dingjust.platform.klotho.platformws.facades;

import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;

import com.dingjust.platform.klotho.platformws.data.UserData;
import com.dingjust.platform.klotho.platformws.dto.SampleWsDTO;
import com.dingjust.platform.klotho.platformws.dto.TestMapWsDTO;


public interface SampleFacades
{
	SampleWsDTO getSampleWsDTO(final String value);

	UserData getUser(String id);

	List<UserData> getUsers();

	SearchPageData<UserData> getUsers(SearchPageData<?> params);

	TestMapWsDTO getMap();
}
