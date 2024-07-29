package com.example.user_service.service;

import com.example.user_service.client.OrderServiceClient;
import com.example.user_service.dto.UserDto;
import com.example.user_service.error.FeignErrorDecoder;
import com.example.user_service.jpa.UserEntity;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final Environment env;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(encoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userId);

        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not fount");
        }

        UserDto userDto = new ModelMapper().map(userEntity.get(), UserDto.class);

//        List<ResponseOrder> orders = new ArrayList<>();
//        String orderUrl = String.format(env.getProperty("order-service.url"), userId);
//        List<ResponseOrder> orderList = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//            new ParameterizedTypeReference<List<ResponseOrder>>() {
//            }).getBody();

        // 써킷브레이커 이전 버전
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);

        log.info("orders 마이크로서비스 호출 전");
        // 써킷 브레이커 적용 후
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orderList = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
            throwable -> new ArrayList<>());
        log.info("orders 마이크로서비스 호출 후");

        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public List<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }
}
