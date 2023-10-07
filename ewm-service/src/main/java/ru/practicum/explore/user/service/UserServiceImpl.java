package ru.practicum.explore.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.exception.ConditionsNotConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        if (repository.findByEmail(newUserRequest.getEmail()).isPresent()) {
            throw new ConditionsNotConflictException("The user with the email=" + newUserRequest.getEmail() + " is already presented");
        }
        User user = repository.save(UserMapper.toUser(newUserRequest));
        return UserMapper.toUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<User> users = ids != null ? repository.findByIdIn(ids, pageable) : (repository.findAll(pageable));
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        return users.stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Integer userId) {
        if (!repository.existsById(userId)) {
            throw new ObjectNotFoundException("User with id=" + userId + " was not found");
        }
        repository.deleteById(userId);
    }
}
