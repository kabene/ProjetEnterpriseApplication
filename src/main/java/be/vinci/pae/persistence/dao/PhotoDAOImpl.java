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
        res.add(toDTO(rs));
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
    PhotoDTO res;
    String query = "SELECT p.* FROM satchofurniture.photos p WHERE p.photo_id = ?";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, photoId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res = toDTO(rs);
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

  /**
   * Make a query to the database to get all the photos visible on the home page's carousel.
   *
   * @return a list containing all the photos visible on the home page's carousel.
   */
  @Override
  public List<PhotoDTO> getAllHomePageVisiblePhotos() {
    List<PhotoDTO> res = new ArrayList<>();
    String query = "SELECT p.* FROM satchofurniture.photos p WHERE p.is_on_home_page = true";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        res.add(toDTO(rs));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }
    return res;
  }



  /**
   * Creates and fills a PhotoDTO object using a ResultSet.
   *
   * @param rs : the ResultSet containing the information.
   * @throws SQLException in case of problem during access to the ResultSet.
   */
  private PhotoDTO toDTO(ResultSet rs) throws SQLException {
    PhotoDTO photoFound = photoFactory.getPhotoDTO();
    photoFound.setPhotoId(rs.getInt("photo_id"));
    photoFound.setFurnitureId(rs.getInt("furniture_id"));
    photoFound.setOnHomePage(rs.getBoolean("is_on_home_page"));
    photoFound.setVisible(rs.getBoolean("is_visible"));
    photoFound.setSource(rs.getString("source"));
    return photoFound;
  }

}
