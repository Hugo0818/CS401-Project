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
        System.out.println("[ResourceManager] Searching catalog with query: '" + query + "' - Total catalog items: " + catalog.size());
        for (Resource resource : catalog) {
            if (resource.getDetails().toLowerCase().contains(query.toLowerCase())) {
                results.add(resource);
                System.out.println("[ResourceManager] Search result: " + resource.getDisplayName() + " - isAvailable: " + resource.isAvailable() + " (Object: " + System.identityHashCode(resource) + ")");
            }
        }
        System.out.println("[ResourceManager] Search returned " + results.size() + " results");
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
    
    public Resource findResourceByNameAndDetails(String displayName, String details) {
        for (Resource r : catalog) {
            if (r.getDisplayName().equals(displayName) && r.getDetails().equals(details)) {
                return r;
            }
        }
        return null;
    }

    public Boolean checkoutResource(Resource resource, Member member) {
        if (resource == null || member == null)
            return false;

        // Find the actual resource in catalog by matching display name and details
        Resource catalogResource = null;
        for (Resource r : catalog) {
            if (r.getDisplayName().equals(resource.getDisplayName()) && 
                r.getDetails().equals(resource.getDetails())) {
                catalogResource = r;
                break;
            }
        }

        if (catalogResource == null) {
            System.out.println("[ResourceManager] Checkout failed: resource not found in catalog");
            return false;
        }

        if (!catalogResource.isAvailable()) {
            System.out.println("[ResourceManager] Checkout failed: resource not available");
            return false;
        }

        catalogResource.setCheckedOut(false); // false = not available (checked out)
        member.addResourceToPossession(catalogResource);
        System.out.println("[ResourceManager] Resource checked out: " + catalogResource.getDisplayName() + " to member " + member.getName() + " (UID: " + member.getUID() + ")");
        System.out.println("[ResourceManager] After checkout - isAvailable: " + catalogResource.isAvailable() + " (Object: " + System.identityHashCode(catalogResource) + ")");
        System.out.println("[ResourceManager] Member now has " + member.getCurrentlyHeldResources().size() + " borrowed items");
        return true;
    }

    public Boolean checkinResource(Resource resource, Member member) {
        if (resource == null || member == null)
            return false;

        // Find the actual resource in catalog by matching display name and details
        Resource catalogResource = null;
        for (Resource r : catalog) {
            if (r.getDisplayName().equals(resource.getDisplayName()) && 
                r.getDetails().equals(resource.getDetails())) {
                catalogResource = r;
                break;
            }
        }

        if (catalogResource == null) {
            System.out.println("[ResourceManager] Checkin failed: resource not found in catalog");
            return false;
        }

        if (catalogResource.isAvailable()) {
            System.out.println("[ResourceManager] Checkin failed: resource is already available (not checked out)");
            return false;
        }

        catalogResource.setCheckedOut(true); // true = available (checked in)
        member.removeResourceFromPossession(catalogResource);
        System.out.println("[ResourceManager] Resource checked in: " + catalogResource.getDisplayName());
        return true;
    }

	public ArrayList<Resource> getAll() {
		System.out.println("[ResourceManager] getAll() called - returning " + catalog.size() + " resources");
		return catalog;
	}

}
