package library;

import java.util.ArrayList;

public class ResourceManager {
    private ArrayList<Resource> catalog;

    public ResourceManager(ArrayList<Resource> catalog) {
        if (catalog != null) {
            this.catalog = catalog;
        } else {
            this.catalog = new ArrayList<>();
        }
    }

    public ArrayList<Resource> searchCatalog(String query) {
        ArrayList<Resource> results = new ArrayList<>();
        for (Resource resource : catalog) {
            if (resource.getDetails().toLowerCase().contains(query.toLowerCase())) {
                results.add(resource);
            }
        }
        return results;
    }

    public Boolean addResource(Resource resource) {
        return catalog.add(resource);
    }

    public Boolean editResource(Resource original, Resource updated) {
        int index = catalog.indexOf(original);
        if (index != -1) {
            catalog.set(index, updated);
            return true;
        }
        return false;
    }

    public Boolean removeResource(Resource resource) {
        return catalog.remove(resource);
    }

    public Boolean checkoutResource(Resource resource, Member member) {
        if (resource == null || member == null)
            return false;

        if (!catalog.contains(resource))
            return false;

        if (!resource.isAvailable())
            return false;

        resource.setCheckedOut(true);
        // member.addBorrowedResource(resource);
        // TODO: this member method does not exist. Fix later
        return true;
    }

    public Boolean checkinResource(Resource resource, Member member) {
        if (resource == null || member == null)
            return false;

        if (!resource.isAvailable())
            return false;

        resource.setCheckedOut(false);
        // member.removeBorrowedResource(resource);
        // TODO: this member method does not exist. Fix later
        return true;
    }

	public Object getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
