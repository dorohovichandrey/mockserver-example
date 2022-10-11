package com.example

import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import org.mockserver.model.Parameter.param
import org.mockserver.verify.VerificationTimes
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName

class MockserverVerifyTests {

    companion object {

        lateinit var mockServerClient: MockServerClient

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            val mockserverVersion = MockServerClient::class.java.getPackage().implementationVersion
            val mockserver = MockServerContainer(
                DockerImageName.parse("mockserver/mockserver:mockserver-$mockserverVersion")
            )
                .withReuse(true)

            mockserver.start()

            RestAssured.port = mockserver.serverPort

            mockServerClient = MockServerClient(mockserver.host, mockserver.serverPort)
        }
    }

    @BeforeEach
    fun beforeEach() {
        mockServerClient.reset()
    }

    @Test
    fun `verify exactly once check should pass` () {
        val json = "{ \"id\": 123 }"
        val request = request()
            .withMethod("GET")
            .withPath("/something/{id}")
            .withPathParameters(param("id"))
        val response = response()
            .withStatusCode(201)
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(json)
        mockServerClient.`when`(request).respond(response)


        When {
            get("/something/123")
        } Then {
            statusCode(201)
        }

        mockServerClient.verify(request, VerificationTimes.exactly(1))
    }

    @Test
    fun `verify exactly two times check should pass` () {
        val json = "{ \"id\": 123 }"
        val request = request()
            .withMethod("GET")
            .withPath("/something/{id}")
            .withPathParameters(param("id"))
        val response = response()
            .withStatusCode(201)
            .withContentType(MediaType.APPLICATION_JSON)
            .withBody(json)
        mockServerClient.`when`(request).respond(response)


        repeat(2) {
            When {
                get("/something/123")
            } Then {
                statusCode(201)
            }
        }

        mockServerClient.verify(request, VerificationTimes.exactly(2))
    }
}
