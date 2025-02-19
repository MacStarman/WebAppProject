package app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import app.Entities.*;
import app.JpaRepository.*;
import app.Vo.DatasetEdit;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(path="/owner")
public class OwnerController {
	@Autowired
	private DatasetRepository datasetRepository;

	@Autowired
	private UserRepository userRepository;

	//Owner home screen
	@GetMapping(path="/{id}")
	public String homePage(@PathVariable int id, Model model) {
		model.addAttribute("id", id);
		return "OwnerHome";
	}

	//C3: Something extra: search engine, for owner
	@GetMapping("/{id}/search")
	public String search(@RequestParam String principal, @PathVariable int id, Model model) {
		List<Dataset> products = new ArrayList<>();
		model.addAttribute("id", id);
		model.addAttribute("products", filterVisibility(principal, products, id));
		return "OwnerSearch";
	}

	//C3: search engine helper method
	private List<Dataset> filterVisibility(String principal, List<Dataset> product, int id) {
		List<Dataset> datasetRepository2 = new ArrayList<>();

		for (Dataset d : datasetRepository.findAll()) {
			datasetRepository2.add(d);
		}

		for (Dataset d : datasetRepository2) {
			if (d.getName().contains(principal) && d.getOwnerId() == id) {
				product.add(d);
			}
		}

		for (Dataset d : product) {
			datasetRepository2.remove(d);
		}

		Collections.sort(product, new Comparator<Dataset>() {
			@Override
			public int compare(Dataset d1, Dataset d2) {
				return d1.getName().compareTo(d2.getName());
			}
		});

		List<Dataset> productsByDesc = new ArrayList<>();

		for (Dataset d : datasetRepository2) {
			if (d.getDescription().contains(principal) && d.getOwnerId() == id) {
				productsByDesc.add(d);
			}
		}

		Collections.sort(productsByDesc, new Comparator<Dataset>() {
			@Override
			public int compare(Dataset d1, Dataset d2) {
				return d1.getDescription().compareTo(d2.getDescription());
			}
		});

		product.addAll(productsByDesc);

		return product;
	}

	//O5: Edit dataset details (post)
	@PostMapping("/edit/{id}/{datasetId}")
	@ResponseBody
	public String editDataset(@PathVariable int id,
							  @PathVariable int datasetId,
							  @Valid @ModelAttribute DatasetEdit Data){
		Dataset item = datasetRepository.getById(datasetId);

		String title = Data.getName();
		String description = Data.getDesc();
		String count = Data.getCount();
		String price = Data.getPrice();

		if(!title.isEmpty()){
			item.setName(title);
		}

		if(!description.isEmpty()){
			item.setDescription(description);
		}

		if(!price.isEmpty()){
			item.setPricePerDataPoint(Float.parseFloat(price));
		}

		if(!count.isEmpty()){
			item.setNumberOfDataPoints(Integer.parseInt(count));
		}

		datasetRepository.save(item);

		return "<meta http-equiv=\"refresh\" content=\"1; url=/owner/edit/" + id + "/" + datasetId + "\">";
	}

	//O5: Edit dataset details (get)
	@GetMapping("/edit/{userId}/{datasetId}")
	public String editDataset(@PathVariable int userId, @PathVariable int datasetId, Model model){
		Dataset d = datasetRepository.getById(datasetId);

		model.addAttribute("id", userId);
		model.addAttribute("datasetId", datasetId);
		model.addAttribute("name", d.getName());
		model.addAttribute("desc", d.getDescription());
		model.addAttribute("count", d.getNumberOfDataPoints());
		model.addAttribute("price", String.format("%.2f", d.getPricePerDataPoint()));
		model.addAttribute("Data", new DatasetEdit());
		return "Edit";
	}

	//O1: Owner can add datasets to the marketplace
	//This is where you enter the details 
	@GetMapping(path="/add/{ownerId}")
	public String addDataset(@PathVariable int ownerId, Model model) {
		model.addAttribute("id", ownerId);
		return "OwnerAdd";
	}
	
	//O1: Owner can add datasets to the marketplace
	//This method creates the new dataset and redirects you back to the dataset list
	@GetMapping(path="/create/{ownerId}")
	public @ResponseBody String newDataset(@RequestParam String name, @RequestParam int id,
										   @RequestParam String desc, @RequestParam int count,
										   @RequestParam float price, @PathVariable int ownerId) {
		Dataset d = new Dataset(id, count, price, false, desc, name, ownerId);

		if(datasetRepository.findById(id).isEmpty()) {
			datasetRepository.save(d);

			return "SUCCESS<meta http-equiv=\"refresh\" content=\"1; url=/owner/" + ownerId + "/show/\">";
		}

		return "FAILURE: ID already taken<meta http-equiv=\"refresh\" content=\"1; url=/owner/add/" + ownerId + "\">";
	}
	
	//Shows owner all available datasets
    @GetMapping(path="{id}/show")
    public String show(@PathVariable int id, Model model){
		model.addAttribute("id", id);

		List<Dataset> products = new ArrayList<>();
		for(Dataset d: datasetRepository.findAll()){
			if(d.getOwnerId() == id){
				products.add(d);
			}
		}

		model.addAttribute("products", products);

		return "OwnerOffers";
    }

    //O2: Hide datasets to make them unavailable to customers
    @GetMapping(path="/show/toggle")
    public @ResponseBody String toggleHideDataset(int id, int user) {
    	Dataset d = datasetRepository.getById(id);
    	d.toggleHidden();
    	datasetRepository.save(d);
    	
    	return "<meta http-equiv=\"refresh\" content=\"1; url=/User/" + user + "/show/\">";
    }
}
