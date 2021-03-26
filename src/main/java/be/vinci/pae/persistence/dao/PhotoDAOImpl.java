package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.factories.PhotoFactory;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhotoDAOImpl implements PhotoDAO {

  @Inject
  private ConnectionBackendDalServices dalServices;
  @Inject
  private PhotoFactory photoFactory;

  @Override
  public List<PhotoDTO> getPhotosByFurnitureId(int furnitureId) {
    List<PhotoDTO> res = new ArrayList<>();
    String query = "SELECT p.* FROM satchofurniture.photos p WHERE p.furniture_id = ?";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, furnitureId);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        PhotoDTO photoDTO = photoFactory.getPhotoDTO();
        photoDTO.setPhotoId(rs.getInt("photo_id"));
        photoDTO.setFurnitureId(rs.getInt("furniture_id"));
        photoDTO.setOnHomePage(rs.getBoolean("is_on_home_page"));
        photoDTO.setVisible(rs.getBoolean("is_visible"));
        photoDTO.setSource(rs.getString("source"));
        res.add(photoDTO);
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }
    return res;
  }

  @Override
  public PhotoDTO getPhotoById(int photoId) {
    PhotoDTO res = photoFactory.getPhotoDTO();
    String query = "SELECT p.* FROM satchofurniture.photos p WHERE p.photo_id = ?";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, photoId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res.setPhotoId(rs.getInt("photo_id"));
        res.setFurnitureId(rs.getInt("furniture_id"));
        res.setOnHomePage(rs.getBoolean("is_on_home_page"));
        res.setVisible(rs.getBoolean("is_visible"));
        res.setSource(rs.getString("source"));
      } else {
        throw new NotFoundException("Error: photo not found");
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }
    return res;
  }
}
