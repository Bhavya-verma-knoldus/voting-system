package candidate.domain

import candidate.api.{AddCandidateCommand, CandidateResponse, ChangeNameCommand, GetCandidateFromIdRequest, VoteCommand}
import kalix.scalasdk.testkit.EventSourcedResult
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

class CandidateSpec extends AnyWordSpec with Matchers {
  "The Candidate" should {

    "correctly process commands of type Create" in {
      val testKit = CandidateTestKit(new Candidate(_))

      val candidateId = "bhavya123"
      val region = "Delhi"
      val name = "Bhavya"

      val result = createCandidate(testKit, candidateId, region, name)
      result.events should have size 1

      val candidateCreated = result.nextEvent[CandidateCreated]
      candidateCreated.candidateId shouldBe candidateId
      candidateCreated.name shouldBe name
      candidateCreated.region shouldBe region
    }

    "correctly process commands of type GetCandidateFromId" in {
      val testKit = CandidateTestKit(new Candidate(_))

      val candidateId = "bhavya123"
      val region = "Delhi"
      val name = "Bhavya"
      createCandidate(testKit, candidateId, region, name)

      val getCandidateFromIdRequest = GetCandidateFromIdRequest(
        candidateId = candidateId
      )

      val result = testKit.getCandidateFromId(getCandidateFromIdRequest)
      val candidate = result.reply

      candidate.candidateId shouldBe candidateId
      candidate.region shouldBe region
      candidate.name shouldBe name
    }

    "correctly process commands of type ChangeName" in {
      val testKit = CandidateTestKit(new Candidate(_))

      val candidateId = "bhavya123"
      val region = "Delhi"
      val name = "Bhavya"
      createCandidate(testKit, candidateId, region, name)

      val newName = "John"
      val changeNameCommand = ChangeNameCommand(
        candidateId = candidateId,
        newName = newName
      )

      val result = testKit.changeName(changeNameCommand)
      result.events should have size 1

      val candidateNameUpdated = result.nextEvent[CandidateNameUpdated]
      candidateNameUpdated.newName shouldBe newName
    }

    "correctly process commands of type VoteForCandidate" in {
      val testKit = CandidateTestKit(new Candidate(_))

      val candidateId = "bhavya123"
      val region = "Delhi"
      val name = "Bhavya"
      createCandidate(testKit, candidateId, region, name)

      val voteCommand = VoteCommand(
        candidateId = candidateId
      )

      val result = testKit.voteForCandidate(voteCommand)
      result.events should have size 1

      val candidateVoted = result.nextEvent[CandidateVoted]
      candidateVoted.candidateId shouldBe candidateId
    }
  }

  private def createCandidate(testKit: CandidateTestKit,candidateId: String, region: String, name: String): EventSourcedResult[CandidateResponse] = {
    val addCandidateCommand = AddCandidateCommand(
      candidateId = candidateId,
      region = region,
      name = name
    )

    testKit.create(addCandidateCommand)
  }
}
