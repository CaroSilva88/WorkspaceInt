package com.ejemplo.salvo;

import com.ejemplo.salvo.model.Game;
import com.ejemplo.salvo.model.GamePlayer;
import com.ejemplo.salvo.model.Player;
import com.ejemplo.salvo.model.Salvo;
import com.ejemplo.salvo.model.Score;
import com.ejemplo.salvo.model.Ship;
import com.ejemplo.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;



@SpringBootApplication
public class SalvoApplication extends  SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner Player(PlayerRepository repoPlay, GameRepository repoGame, GamePlayerRepository repoGP,
									ShipRepository shipRepository, SalvoRepository salvoRepository,
									ScoreRepository scoreRepository) {
		return (args) -> {

			//create player
			Player p1 = new Player("j.bauer@ctu.gov", this.passwordEncoder().encode("Player1"));
			Player p2 = new Player( "c.obrian@ctu.gov", this.passwordEncoder().encode("Player2"));
			Player p3 = new Player( "kim_bauer@gmail.com", this.passwordEncoder().encode("Player3"));
			Player p4 = new Player( "t.alemida@ctu.gov", this.passwordEncoder().encode("Player4"));

			// save player
			repoPlay.save(p1);
			repoPlay.save(p2);
			repoPlay.save(p3);
			repoPlay.save(p4);

			//create game
			Game g1 = new Game(new Date());
			Game g2 = new Game(Date.from(new Date().toInstant().plusSeconds(3600)));
			Game g3 = new Game(Date.from(new Date().toInstant().plusSeconds(7200)));

			//save games
			repoGame.save(g1);
			repoGame.save(g2);
			repoGame.save(g3);


			//create gamePlayer
			//Date dt = new Date();

			GamePlayer gp1 = new GamePlayer(g1,p1);
			GamePlayer gp2 = new GamePlayer(g1,p2);

			GamePlayer gp3 = new GamePlayer(g2,p3);
			GamePlayer gp4 = new GamePlayer(g2,p4);

			GamePlayer gp5 = new GamePlayer(g3, p4);
			GamePlayer gp6 = new GamePlayer(g3, p3);

			//save gamePlayers
			repoGP.save(gp1);
			repoGP.save(gp2);
			repoGP.save(gp3);
			repoGP.save(gp4);
			repoGP.save(gp5);
			repoGP.save(gp6);


			//create Ship

			//Ship ship1 = new Ship("Destroyer", gp1, Arrays.asList("H2","H3","H4"));
			//Ship ship2 = new Ship("Submarine", gp2, Arrays.asList("E1","F1","G1"));
			//Ship ship3 = new Ship("Patrol Boat", gp3, Arrays.asList("B4","B5"));
			//Ship ship4 = new Ship("Destroyer", gp4, Arrays.asList("B5","C5","D5"));
			//Ship ship5 = new Ship("Patrol Boat ", gp5, Arrays.asList("F1","F2"));
			//Ship ship6 = new Ship("Destroyer", gp6, Arrays.asList("B5","C5","D5"));
			//Ship ship7 = new Ship("Patrol Boat", gp1, Arrays.asList("C6","C7"));
			//Ship ship8 = new Ship("Submarine", gp2, Arrays.asList("A2","A3","A4"));
			//Ship ship9 = new Ship("Patrol Boat ", gp3, Arrays.asList("G6","H6"));

			//save ships
			//shipRepository.save(ship1);
			//shipRepository.save(ship2);
			//shipRepository.save(ship3);
			//shipRepository.save(ship4);
			//shipRepository.save(ship5);
			//shipRepository.save(ship6);
			//shipRepository.save(ship7);
			//shipRepository.save(ship8);
			//shipRepository.save(ship9);

			//create Salvo
			/*Salvo sal1 = new Salvo(1, gp1, Arrays.asList("B5", "C5", "F1"));
			Salvo sal2 = new Salvo(1, gp2, Arrays.asList("B4", "B5", "B6"));
			Salvo sal3 = new Salvo(2, gp1, Arrays.asList("F2", "D5" ));
			Salvo sal4 = new Salvo(2, gp2, Arrays.asList("E1", "H3", "A2"));

			Salvo sal5 = new Salvo(1, gp3, Arrays.asList("A2", "A4", "G6"));
			Salvo sal6 = new Salvo(1, gp4, Arrays.asList("B5", "D5", "C7"));
			Salvo sal7 = new Salvo(2, gp3, Arrays.asList("A3", "H6"));
			Salvo sal8 = new Salvo(2, gp4, Arrays.asList("C5", "C6"));

			Salvo sal9 = new Salvo(1, gp5, Arrays.asList( "G6", "H6", "A4"));
			Salvo sal10 = new Salvo(1, gp6, Arrays.asList( "H1", "H2", "H3"));
			Salvo sal11 = new Salvo(2, gp5, Arrays.asList( "A2", "A3", "D8"));
			Salvo sal12 = new Salvo(2, gp6, Arrays.asList( "E1", "F2", "G3"));

			//save Salvos

			salvoRepository.save(sal1);
			salvoRepository.save(sal2);
			salvoRepository.save(sal3);
			salvoRepository.save(sal4);
			salvoRepository.save(sal5);
			salvoRepository.save(sal6);
			salvoRepository.save(sal7);
			salvoRepository.save(sal8);
			salvoRepository.save(sal9);
			salvoRepository.save(sal10);
			salvoRepository.save(sal11);
			salvoRepository.save(sal12);*/

			//crate score

			Score score1 = new Score(g1, p1, 0.5);
			Score score2 = new Score(g1, p2, 1);

			Score score3 = new Score(g2, p3, 1);
			Score score4 = new Score(g2, p4, 0.5);

			Score score5 = new Score(g3, p1, 1);
			Score score6 = new Score(g3, p4, 1);


			//save scores
			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}




@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository  playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return  new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/web/game.html").hasAuthority("USER")
				.antMatchers("/api/**").permitAll()
				.antMatchers("/h2-console/**").permitAll().anyRequest().authenticated()
				.and().csrf().ignoringAntMatchers("/h2-console/**")
				.and().headers().frameOptions().sameOrigin();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}
