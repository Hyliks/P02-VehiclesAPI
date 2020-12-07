package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;


	@Test
	public void addPrice() {
		Price price = new Price("USD",new BigDecimal(1.00),new Long(6));
		HttpEntity<Price> request = new HttpEntity<>(price);
		ResponseEntity<Price> answer = this.restTemplate.postForEntity("http://localhost:" + port + "/prices/",request, Price.class);

		assertThat(answer.getBody(), notNullValue());
		assertThat(answer.getBody().getVehicleId(), is(price.getVehicleId()));
		assertThat(answer.getBody().getCurrency(), is(price.getCurrency()));
		assertThat(answer.getBody().getPrice(), is(price.getPrice()));
	}

	@Test
	public void getPrice() throws InterruptedException{
		Price price = new Price("USD",new BigDecimal(1.00),new Long(6));

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-COM-PERSIST", "true");

		HttpEntity<Price> request = new HttpEntity<>(price, headers);
		ResponseEntity<Price> answer = this.restTemplate.postForEntity("http://localhost:" + port + "/prices/",request, Price.class);

		assertThat(answer.getBody(), notNullValue());
		assertThat(answer.getBody().getVehicleId(), is(price.getVehicleId()));
		assertThat(answer.getBody().getCurrency(), is(price.getCurrency()));
		assertThat(answer.getBody().getPrice(), is(price.getPrice()));

		ResponseEntity<Price> response = this.restTemplate.getForEntity("http://localhost:" + port + "/prices/" + 1 + "/",Price.class);
		assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));

		assertThat(response.getBody(), notNullValue());
		assertThat(response.getBody().getVehicleId(), is(price.getVehicleId()));
		assertThat(response.getBody().getCurrency(), is(price.getCurrency()));
	}

	@Test
	public void getPrices() {
		Price price = new Price("USD",new BigDecimal(1.00),new Long(6));

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-COM-PERSIST", "true");

		HttpEntity<Price> request = new HttpEntity<>(price, headers);
		ResponseEntity<Price> answer = this.restTemplate.postForEntity("http://localhost:" + port + "/prices/",request, Price.class);

		assertThat(answer.getBody(), notNullValue());
		assertThat(answer.getBody().getVehicleId(), is(price.getVehicleId()));
		assertThat(answer.getBody().getCurrency(), is(price.getCurrency()));
		assertThat(answer.getBody().getPrice(), is(price.getPrice()));

		price = new Price("USD",new BigDecimal(200),new Long(7));

		headers = new HttpHeaders();
		headers.set("X-COM-PERSIST", "true");

		request = new HttpEntity<>(price, headers);
		answer = this.restTemplate.postForEntity("http://localhost:" + port + "/prices/",request, Price.class);

		assertThat(answer.getBody(), notNullValue());
		assertThat(answer.getBody().getVehicleId(), is(price.getVehicleId()));
		assertThat(answer.getBody().getCurrency(), is(price.getCurrency()));
		assertThat(answer.getBody().getPrice(), is(price.getPrice()));


		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = this.restTemplate.exchange("http://localhost:" + port + "/prices", HttpMethod.GET,requestEntity,String.class);
		System.out.println(response.getBody());
		assertThat(response.getStatusCode(),equalTo(HttpStatus.OK));
	}

}
