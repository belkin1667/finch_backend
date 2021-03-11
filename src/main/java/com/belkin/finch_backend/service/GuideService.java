package com.belkin.finch_backend.service;

import com.belkin.finch_backend.dao.interfaces.GuideDAO;
import com.belkin.finch_backend.api.dto.AccessType;
import com.belkin.finch_backend.api.dto.GuideResponse;
import com.belkin.finch_backend.exception.notfound.GuideNotFoundException;
import com.belkin.finch_backend.model.Guide;
import com.belkin.finch_backend.util.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuideService {

    private GuideDAO guideDAO;
    private UserService userService;

    @Autowired
    public GuideService(@Qualifier("guide_fake") GuideDAO guideDAO,
                        UserService userService) {
        this.guideDAO = guideDAO;
        this.userService = userService;
    }

    public List<GuideResponse> getUserGuidesPreview(String myUsername, String requestedUsername) {
        List<Guide> requestedGuides = guideDAO.readAllGuidesByAuthorUsername(requestedUsername);
        AccessType accessType;
        if (myUsername.equals(requestedUsername)) {
            accessType = AccessType.ME_PARTIAL_ACCESS;
        } else {
            accessType = AccessType.NOT_ME_PARTIAL_ACCESS;
        }
        return requestedGuides.stream()
                .map(g -> new GuideResponse(g, accessType))
                .collect(Collectors.toList());
    }

    public List<GuideResponse> getUserGuides(String myUsername, String requestedUsername) {
        List<Guide> requestedGuides = guideDAO.readAllGuidesByAuthorUsername(requestedUsername);
        AccessType accessType = userService.getAccessType(myUsername, requestedUsername);
        if (accessType != null)
            return requestedGuides.stream()
                    .map(g -> new GuideResponse(g, accessType))
                    .collect(Collectors.toList());
        else
            return null;
    }

    public Base62 addGuide(String authorUsername, Guide guide) {
        guide.setAuthorUsername(authorUsername);
        return guideDAO.createGuide(guide);
    }

    public boolean editGuide(String authorUsername, Guide guide) {
        if (isUserGuideAuthor(authorUsername, guide.getId()))
            return guideDAO.updateGuideById(guide.getId(), guide);
        else return false;
    }

    public boolean deleteGuide(String authorUsername, Base62 id) {
        if (isUserGuideAuthor(authorUsername, id))
            return guideDAO.deleteGuideById(id);
        else return false;
    }

    public GuideResponse getUserGuide(String myUsername, Base62 id) {
        Guide guide = guideDAO.readGuideById(id).orElseThrow(() -> new GuideNotFoundException(id));
        return new GuideResponse( guide, guide.getAuthorUsername().equals(myUsername) ? AccessType.ME_FULL_ACCESS : AccessType.NOT_ME_FULL_ACCESS);
    }

    public boolean isUserGuideAuthor(String authorUsername, Base62 id) {
        Guide guide = guideDAO.readGuideById(id).orElseThrow(() -> new GuideNotFoundException(id));
        return guide.getAuthorUsername().equals(authorUsername);
    }
}
