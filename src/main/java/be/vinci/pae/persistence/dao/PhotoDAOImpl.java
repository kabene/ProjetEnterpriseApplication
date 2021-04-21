package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.factories.PhotoFactory;
import be.vinci.pae.exceptions.NotFoundException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhotoDAOImpl extends AbstractDAO implements PhotoDAO {

  @Inject
  private PhotoFactory photoFactory;

  /**
   * Finds all photos for a given furniture id.
   *
   * @param furnitureId : the furniture id.
   * @return a list of photoDTO
   */
  @Override
  public List<PhotoDTO> findAllByFurnitureId(int furnitureId) {
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

  /**
   * Finds one photo by id.
   *
   * @param photoId : the researched photoId.
   * @return one instance of PhotoDTO
   */
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
   * Finds all entries of photos in the DB.
   *
   * @return a list of photoDTO
   */
  public List<PhotoDTO> findAll() {
    return findAll("photos");
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
   * updates the is_visible column of an entry in the database.
   *
   * @param photoDTO : dto containing the id and new is_visible flag.
   * @return dto containing modified entry
   */
  @Override
  public PhotoDTO updateIsVisible(PhotoDTO photoDTO) {
    String query = "UPDATE satchofurniture.photos "
        + "SET is_visible = ? "
        + "WHERE photo_id = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setBoolean(1, photoDTO.isVisible());
      ps.setInt(2, photoDTO.getPhotoId());
      ps.execute();
      ps.close();
    } catch(SQLException e) {
      throw new InternalError(e);
    }
    return photoDTO;
  }

  /**
   * Creates and fills a PhotoDTO object using a ResultSet.
   *
   * @param rs : the ResultSet containing the information.
   * @return a dto containing the information from the result set
   * @throws SQLException in case of problem during access to the ResultSet.
   */
  @Override
  protected PhotoDTO toDTO(ResultSet rs) throws SQLException {
    PhotoDTO photoFound = photoFactory.getPhotoDTO();
    photoFound.setPhotoId(rs.getInt("photo_id"));
    photoFound.setFurnitureId(rs.getInt("furniture_id"));
    photoFound.setOnHomePage(rs.getBoolean("is_on_home_page"));
    photoFound.setVisible(rs.getBoolean("is_visible"));
    photoFound.setSource(rs.getString("source"));
    return photoFound;
  }

}
