package ind.automation.salesexpense.repositoryimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ind.automation.salesexpense.entity.MasterVendorListEntity;
import ind.automation.salesexpense.repository.MasterVendorListRepository;

@Transactional
@Repository
public class MasterVendorListRepositoryImplementation implements MasterVendorListRepository {

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<MasterVendorListEntity> getMasterVendorListData() {
		try {
			Session currentSession = entityManager.unwrap(Session.class);
			Query<MasterVendorListEntity> query = currentSession.createQuery("from MasterVendorListEntity",
					MasterVendorListEntity.class);
			return query.getResultList();
		} catch (NullPointerException ex) {
			return null;
		}
	}

	@Override
	public void saveNewAccountDetails(MasterVendorListEntity entity) {
		try {
			Session currentSession = entityManager.unwrap(Session.class);
			currentSession.save(entity);
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	@Override
	public void updateNewAccountDetails(MasterVendorListEntity entity) {
		try {
			Session currentSession = entityManager.unwrap(Session.class);
			currentSession.update(entity);
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	@Override
	public List<MasterVendorListEntity> getMasterDetailByVendor(String vendorName) {
		try {
			Session currentSession = entityManager.unwrap(Session.class);
			Query<MasterVendorListEntity> query = currentSession.createQuery(
					"from MasterVendorListEntity where vendorName= :vendorName", MasterVendorListEntity.class);
			query.setParameter("vendorName", vendorName);
			return query.getResultList();
		} catch (NullPointerException ex) {
			return null;
		} catch (Exception ex) {
			ex.getMessage();
			return null;
		}
	}
}