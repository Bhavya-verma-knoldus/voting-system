package candidate.domain

import candidate.api
import candidate.api.CandidateResponse
import kalix.scalasdk.eventsourcedentity.EventSourcedEntity
import kalix.scalasdk.eventsourcedentity.EventSourcedEntityContext

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class Candidate(context: EventSourcedEntityContext) extends AbstractCandidate {
  val log = org.slf4j.LoggerFactory.getLogger(classOf[Candidate])

  override def emptyState: CandidateState = CandidateState.defaultInstance

  override def create(currentState: CandidateState, addCandidateCommand: api.AddCandidateCommand): EventSourcedEntity.Effect[api.CandidateResponse] = {
    if (currentState.candidateId.nonEmpty) {
      val response = CandidateResponse(
        reply = "Candidate with this id already exists, kindly create a candidate with a different id"
      )
      effects.reply(response)
    }
    else {
      validate(addCandidateCommand).getOrElse(handle(currentState, addCandidateCommand))
    }
  }

  override def getCandidateFromId(currentState: CandidateState, getCandidateFromIdRequest: api.GetCandidateFromIdRequest): EventSourcedEntity.Effect[api.Candidate] = {
    if (currentState.candidateId.isEmpty) {
      effects.error(s"Candidate with id ${getCandidateFromIdRequest.candidateId} does not exists")
    }
    else {
      val candidate = toApi(currentState)
      effects.reply(candidate)
    }
  }

  override def changeName(currentState: CandidateState, changeNameCommand: api.ChangeNameCommand): EventSourcedEntity.Effect[api.CandidateResponse] = {
    if (!currentState.candidateId.equals(changeNameCommand.candidateId) || currentState.candidateId.isEmpty) {
      effects.error(s"Candidate with id ${changeNameCommand.candidateId} does not exists")
    }
    else {
      validate(changeNameCommand).getOrElse(handle(currentState, changeNameCommand))
    }
  }

  override def voteForCandidate(currentState: CandidateState, voteCommand: api.VoteCommand): EventSourcedEntity.Effect[api.CandidateResponse] = {
    if (!currentState.candidateId.equals(voteCommand.candidateId) || currentState.candidateId.isEmpty) {
      effects.error(s"Candidate with id ${voteCommand.candidateId} does not exists")
    }
    else {
      validate(voteCommand).getOrElse(handle(currentState, voteCommand))
    }
  }

  override def candidateCreated(currentState: CandidateState, candidateCreated: CandidateCreated): CandidateState =
    currentState.copy(candidateId = candidateCreated.candidateId, region = candidateCreated.region, name = candidateCreated.name)

  override def candidateNameUpdated(currentState: CandidateState, candidateNameUpdated: CandidateNameUpdated): CandidateState =
    currentState.copy(candidateId = candidateNameUpdated.candidateId, name = candidateNameUpdated.newName)

  override def candidateVoted(currentState: CandidateState, candidateVoted: CandidateVoted): CandidateState =
    currentState.copy(candidateId = candidateVoted.candidateId, votes = currentState.votes + 1)

  private def validate(command: api.AddCandidateCommand): Option[EventSourcedEntity.Effect[api.CandidateResponse]] = {
    if (command.candidateId.isEmpty) {
      Some(effects.error("The candidate id is required"))
    }
    else if (command.name.isEmpty) {
      Some(effects.error("The candidate name is required"))
    }
    else if (command.region.isEmpty) {
      Some(effects.error("The candidate region is required"))
    }
    else {
      None
    }
  }

  private def validate(command: api.ChangeNameCommand): Option[EventSourcedEntity.Effect[api.CandidateResponse]] = {
    if (command.candidateId.isEmpty) {
      Some(effects.error("The candidate id is required"))
    }
    else if (command.newName.isEmpty) {
      Some(effects.error("The candidate new name is required"))
    }
    else {
      None
    }
  }

  private def validate(command: api.VoteCommand): Option[EventSourcedEntity.Effect[api.CandidateResponse]] = {
    if (command.candidateId.isEmpty) {
      Some(effects.error("The candidate id is required"))
    }
    else {
      None
    }
  }

  private def handle(state: CandidateState, command: api.AddCandidateCommand): EventSourcedEntity.Effect[api.CandidateResponse] = {
    log.info("state: {}\nAddCandidateCommand: {}", state, command)

    val response = api.CandidateResponse(
      reply = "Candidate successfully created"
    )

    effects
      .emitEvent(eventFor(command))
      .thenReply(_ => response)
  }

  private def handle(state: CandidateState, command: api.ChangeNameCommand): EventSourcedEntity.Effect[api.CandidateResponse] = {
    log.info("state: {}\nChangeNameCommand: {}", state, command)

    val response = api.CandidateResponse(
      reply = "Name changed successfully"
    )

    effects
      .emitEvent(eventFor(command))
      .thenReply(_ => response)
  }

  private def handle(state: CandidateState, command: api.VoteCommand): EventSourcedEntity.Effect[api.CandidateResponse] = {
    log.info("state: {}\nVoteCommand: {}", state, command)

    val response = api.CandidateResponse(
      reply = "Candidate voted"
    )

    effects
      .emitEvent(eventFor(command))
      .thenReply(_ => response)
  }

  private def eventFor(command: api.AddCandidateCommand): CandidateCreated = {
    CandidateCreated(
      candidateId = command.candidateId,
      region = command.region,
      name = command.name
    )
  }

  private def eventFor(command: api.ChangeNameCommand): CandidateNameUpdated = {
    CandidateNameUpdated(
      candidateId = command.candidateId,
      newName = command.newName
    )
  }

  private def eventFor(command: api.VoteCommand): CandidateVoted = {
    CandidateVoted(
      candidateId = command.candidateId
    )
  }

  private def toApi(currentState: CandidateState) = {
    api.Candidate(
      candidateId = currentState.candidateId,
      region = currentState.region,
      name = currentState.name,
      votes = currentState.votes
    )
  }
}
