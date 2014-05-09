package ch.ergon.littlemachine.server.businesslogic;

public interface TransitionTable {

	public abstract TransitionProcedure nextTransition(MachineState currentState, MachineTransition transition);

}
