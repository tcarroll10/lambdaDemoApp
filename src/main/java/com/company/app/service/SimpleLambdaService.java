package com.company.app.service;

import org.springframework.stereotype.Service;

import com.company.app.to.DataTo;

@Service
public class SimpleLambdaService implements LambdaService {

	@Override
	public DataTo calc(DataTo dataTo) {
		
		return DataTo.builder(dataTo).amount(dataTo.getAmount().movePointRight(2)).build();
	}

}
