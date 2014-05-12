package ch.ergon.littlemachine.server;

import ch.ergon.littlemachine.server.businesslogic.BusinessLogicProcessor;
import ch.ergon.littlemachine.server.businesslogic.BusinessLogicService;
import ch.ergon.littlemachine.server.businesslogic.DefaultBusinessLogicProcessor;
import ch.ergon.littlemachine.server.businesslogic.DefaultBusinessLogicService;
import ch.ergon.littlemachine.server.businesslogic.DefaultTransitionTable;
import ch.ergon.littlemachine.server.businesslogic.TransitionTable;

import com.google.inject.AbstractModule;

public class LittleMachineModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BusinessLogicProcessor.class).to(DefaultBusinessLogicProcessor.class);
		bind(TransitionTable.class).to(DefaultTransitionTable.class);
		bind(BusinessLogicService.class).to(DefaultBusinessLogicService.class);
	}

}
