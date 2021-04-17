package com.belkin.finch_backend.service;

import com.belkin.finch_backend.api.dto.UserResponse;
import com.belkin.finch_backend.dao.interfaces.UserDAO;
import com.belkin.finch_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final UserDAO userDAO;
    private final UserService userService;


    @Autowired
    public SearchService(@Qualifier("database_user") UserDAO userDAO,
                         UserService userService) {
        this.userDAO = userDAO;
        this.userService = userService;
    }

    public List<UserResponse> searchUser(String myUsername, String query) {
        Optional<User> maybeUser = userDAO.findById(query);
        List<UserResponse> searchResults = new ArrayList<>();
        if (maybeUser.isEmpty()) {
            LinkedHashSet<User> searchResultSet = new LinkedHashSet<>();
            searchResultSet.addAll(userDAO.findByUsernameContainsIgnoreCase(query));
            searchResultSet.addAll(userDAO.findByTitleContainsIgnoreCase(query));
            searchResults = searchResultSet.stream().map(u -> new UserResponse(u, userService.getAccessType(myUsername, u.getUsername())))
                    .limit(10).collect(Collectors.toList());
        }
        else {
            searchResults.add(new UserResponse(maybeUser.get(), userService.getAccessType(myUsername, maybeUser.get().getUsername())));
        }
        return searchResults;
    }

}
