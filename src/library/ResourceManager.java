package library;

import java.util.ArrayList;

public class ResourceManager {
    private ArrayList<Resource> catalog;
    private String filepath;

    public ResourceManager(String filepath) {
        this.filepath = filepath;
        this.catalog = new ArrayList<>();
        if (this.filepath != null) loadFromDisk();
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
        // Placeholder: implement checkout logic as needed
        return false;
    }

    public Boolean checkinResource(Resource resource, Member member) {
        // Placeholder: implement checkin logic as needed
        return false;
    }

    public void saveChanges() {
        try (java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(filepath))) {
            out.writeObject(catalog);
        } catch (Exception e) {
            // Optionally log or handle the error
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromDisk() {
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(filepath))) {
            catalog = (ArrayList<Resource>) in.readObject();
        } catch (Exception e) {
            catalog = new ArrayList<>(); // fallback to empty list if file not found or error
        }
    }
}
