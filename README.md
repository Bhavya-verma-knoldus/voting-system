# Voting-system with KALIX

---

This project is a demonstration of Event Sourcing with KALIX.</br>
In this project we can perform some operations like:</br>
1. [ Create a candidate. ](#create)
2. [ Get candidate from id. ](#getCandidateFromId)
3. [ Change name of the candidate. ](#changeName)
4. [ Vote for the candidate. ](#voteForCanddate)

<a name="create"></a>
## 1. Create a candidate.

To create a candidate, you have to call the `create` method of `CandidateService` from the gRPCUI or you
can hit the endpoint with the required request json shown as below:</br></br>
endpoint -> `http://localhost:8080/candidate/create` </br>
methodType -> POST</br>
request  ->
```
{
  "candidateId": "bhavya123",
  "region": "Delhi",
  "name": "Bhavya"
}
```

As a response, you will get the below message:
```
{
    "reply": "Candidate successfully created"
}
```

<a name="getCandidateFromId"></a>
## 2. Get candidate from id.

To get a candidate from id, you have to call the `getCandidateFromId` method of `CandidateService` from the gRPCUI or you
can hit the endpoint with the specific candidate_id:</br></br>
endpoint -> `http://localhost:8080/candidate/{candidate_id}` </br>
methodType -> GET</br>

example -> `http://localhost:8080/candidate/bhavya123`

As a response, you will get:
```
{
    "candidateId": "bhavya123",
    "region": "Delhi",
    "name": "Bhavya",
    "votes": 0
}
```


<a name="changeName"></a>
## 3. Change name of the candidate.

To change the name of the candidate, you have to call the `changeName` method of `CandidateService` from the gRPCUI or you
can hit the endpoint with the required request json shown as below:</br></br>
endpoint -> `http://localhost:8080/candidate/{candidate_id}/change/name` </br>
methodType -> PUT</br>

example -> `http://localhost:8080/candidate/bhavya123/change/name`

request  ->
```
{
  "new_name": "Something"
}
```

As a response, you will get:
```
{
    "reply": "Name changed successfully"
}
```

<a name="voteForCanddate"></a>
## 4. Vote for the candidate.

To vote for the candidate, you have to call the `voteForCandidate` method of `CandidateService` from the gRPCUI or you
can hit the endpoint with the required request json shown as below:</br></br>
endpoint -> `http://localhost:8080/candidate/{candidate_id}/vote` </br>
methodType -> POST</br>

example -> `http://localhost:8080/candidate/bhavya123/vote`

As a response, you will get the below message:
```
{
    "reply": "Candidate voted"
}
```

## Package and deploy this project


To build and publish the container image and then deploy the service, follow these steps:

1. Use the `Docker/publish` task to build the container image and publish it to your container registry. At the end of this command sbt will show you the container image URL you’ll need in the next part of this process.
```
    sbt Docker/publish -Ddocker.username=[your-docker-hub-username]`
```

2. If you haven’t done so yet, sign in to your Kalix account.
   If this is your first time using Kalix, this will let you register an account,
   [create your first project](https://docs.kalix.io/projects/create-project.html), and set this project as the default.
```
    kalix auth login
```

3. [Deploy the service](https://docs.kalix.io/services/deploy-service.html#_deploy) with the published container image from above:
```
    kalix service deploy <service name> <container image>
```
4. You can [verify the status of the deployed](https://docs.kalix.io/services/deploy-service.html#_verify_service_status) service using:
```
    kalix service list
```


## Invoke your service

Once the service has started successfully, you can [start a proxy locally](https://docs.kalix.io/services/invoke-service.html#_testing_and_development) to access the service:
```
    kalix service proxy <service name> --grpcui
```

The --grpcui option also starts and opens a gRPC web UI for exploring and invoking the service (available at http://127.0.0.1:8080/ui/).

Or you can use postman and other option to hit the above-mentioned endpoints with correspondence requests.