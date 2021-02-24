package com.gofundme.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.gofundme.server.model.AddressModel
import com.gofundme.server.model.UserModel
import com.gofundme.server.requestHandler.RegisterHandler
import org.junit.jupiter.api.*
import org.junit.jupiter.api.condition.EnabledOnJre
import org.junit.jupiter.api.condition.JRE
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ServerApplication::class]
)
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DisplayName("GO-FUND-ME INTEGRATION TESTS")
class ServerApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper



    @Test
    @Order(1)
    @DisplayName("/api/v1/auth/register  -Should Pass")
    @EnabledOnJre(JRE.JAVA_8,disabledReason = "Server was programmed to run on Java 8")
    fun registerUser(){

        // GIVEN
        val registrationDetails= RegisterHandler(username = "jos123",email = "jos@gmail.com",password = "123456",country = "Kenya",city = "Nairobi")
        val jsonBody=objectMapper.writeValueAsString(registrationDetails)

        // WHEN
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/register").secure(true).content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(
                MediaType.APPLICATION_JSON)
        )
        // EXPECTATIONS
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())


    }
    @Test
    @Order(2)
    @DisplayName("/api/v1/auth/register -Should Fail")
    @EnabledOnJre(JRE.JAVA_8,disabledReason = "Server was programmed to run on Java 8")
    fun doNotRegisterUser(){

        //GIVEN WRONG DETAILS
        val registrationDetails= RegisterHandler(username = "23",email = "jos.com",password = "1",country = "Kenya",city = "Nairobi")
        val jsonBody=objectMapper.writeValueAsString(registrationDetails)

        // WHEN
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/register").secure(true).content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(
                MediaType.APPLICATION_JSON)
        )

            // EXPECTATIONS
            .andExpect(MockMvcResultMatchers.status().isBadRequest)




    }

}
