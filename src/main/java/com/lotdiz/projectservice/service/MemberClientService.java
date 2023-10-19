package com.lotdiz.projectservice.service;

import com.lotdiz.projectservice.dto.response.GetProjectInfoForLikesResponseDto;
import com.lotdiz.projectservice.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberClientService {

  private final ProjectRepository projectRepository;

  @Transactional(readOnly = true)
  public List<GetProjectInfoForLikesResponseDto> getProjectInfoForLikes(
      List<Long> projectIds) {
    return projectRepository.findProjectInfoForLikes(projectIds);
  }
}
