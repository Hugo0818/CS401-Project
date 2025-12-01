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
        boolean result = catalog.add(resource);
        System.out.println("[ResourceManager] Resource added: " + resource.getDisplayName() + " - Total resources: " + catalog.size());
        return result;
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
        // Find and remove by matching display name and details since equals() is not implemented
        // TODO: this is a temporary workaround; consider implementing equals() in Resource classes
        for (int i = 0; i < catalog.size(); i++) {
            Resource r = catalog.get(i);
            if (r.getDisplayName().equals(resource.getDisplayName()) && 
                r.getDetails().equals(resource.getDetails())) {
                catalog.remove(i);
                System.out.println("[ResourceManager] Resource removed: " + resource.getDisplayName() + " - Total resources: " + catalog.size());
                return true;
            }
        }
        System.out.println("[ResourceManager] Failed to remove resource: " + resource.getDisplayName() + " - not found in catalog");
        return false;
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

	public ArrayList<Resource> getAll() {
		System.out.println("[ResourceManager] getAll() called - returning " + catalog.size() + " resources");
		return catalog;
	}

}
