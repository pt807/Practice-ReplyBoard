package com.board_test.demo;

import com.board_test.demo.domain.Board;
import com.board_test.demo.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.lang.module.FindException;
import java.net.BindException;
import java.util.stream.IntStream;

@EnableJpaAuditing
@SpringBootApplication
public class BoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner runner(BoardRepository boardRepository)throws Exception {
//
//		return (args) -> {
//			IntStream.range(1, 200).forEach(index ->
//					boardRepository.save(Board.builder()
//							.content("hi" + index)
//							.writer("test" + index)
//							.title("안녕하세요" + index)
//							.build()
//					));
//		};
//	}
}
