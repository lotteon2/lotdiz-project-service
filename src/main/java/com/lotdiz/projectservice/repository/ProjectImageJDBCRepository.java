package com.lotdiz.projectservice.repository;

import com.lotdiz.projectservice.entity.ProjectImage;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectImageJDBCRepository {
  private final JdbcTemplate jdbcTemplate;

  public void bachInsert(List<ProjectImage> projectImageList) {
    String sql =
        "INSERT INTO project_image ("
            + "project_id, project_image_url, project_image_is_thumbnail, created_at, updated_at"
            + ") VALUES (?, ?, ?, ?, ?)";
    jdbcTemplate.batchUpdate(
        sql,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ProjectImage projectImage = projectImageList.get(i);
            ps.setLong(1, projectImage.getProject().getProjectId());
            ps.setString(2, projectImage.getProjectImageUrl());
            ps.setBoolean(3, projectImage.getProjectImageIsThumbnail());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
          }

          @Override
          public int getBatchSize() {
            return projectImageList.size();
          }
        });
  }
}
