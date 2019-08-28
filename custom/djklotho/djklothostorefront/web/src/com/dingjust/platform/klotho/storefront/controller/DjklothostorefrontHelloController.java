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
package com.dingjust.platform.klotho.storefront.controller;

import com.dingjust.platform.klotho.platformws.data.UserData;
import com.dingjust.platform.klotho.storefront.security.annotation.DjLoginRequired;
import de.hybris.platform.commercefacades.user.converters.populator.PrincipalPopulator;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class DjklothostorefrontHelloController
{
	@Autowired
	private SearchRestrictionService searchRestrictionService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private UserService userService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	@DjLoginRequired
	@ResponseBody
	public List<UserData> printWelcome()
	{
        userService.setCurrentUser(userService.getUserForUID("liuzehua"));
		searchRestrictionService.enableSearchRestrictions();
		FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {User}");
		List<UserModel> userModels =  flexibleSearchService.<UserModel>search(query).getResult();
		userModels.stream().forEach(userModel -> System.out.println(userModel.getUid()));
		List<UserData> userData = new ArrayList<>();
		this.populate(userModels, userData);
		return userData;
	}

	private void populate(List<UserModel> userModels, List<UserData> userData) {
		userModels.stream().forEach(userModel -> {
			UserData user = new UserData();
			user.setDescription(userModel.getUid() + userModel.getName());
			userData.add(user);
		});
	}
}
