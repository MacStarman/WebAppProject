package app.Controllers;

import app.Entities.Dataset;
import app.Entities.Review;
import app.HTMLString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GeneralController {
	@Autowired
	private app.JpaRepository.DatasetRepository datasetRepository;

	@Autowired
	private app.JpaRepository.ReviewRepository reviewRepository;

	//Home page
	@GetMapping(path="/")
	public String homeScreen(){
		return "Home";
	}

	//C3: Something extra - search bar
	@GetMapping("/search")
	public String search(@RequestParam String principal, Model model) {
        List<Dataset> products = new ArrayList<>();
        model.addAttribute("products", filterVisibility(principal, products));
        return "Search";
	}

	//C3: Something extra - search bar, helper function
	private List<Dataset> filterVisibility(String principal, List<Dataset> product) {
		List<Dataset> datasetRepository2 = new ArrayList<>();

		for(Dataset d: datasetRepository.findAll()){
			datasetRepository2.add(d);
		}

		for(Dataset d : datasetRepository2){
			if(d.getName().contains(principal) && !d.isHidden()) {
				product.add(d);
			}
		}

		for(Dataset d: product){
			datasetRepository2.remove(d);
		}

		Collections.sort(product, new Comparator<Dataset>() {
			@Override
			public int compare(Dataset d1, Dataset d2){
				return d1.getName().compareTo(d2.getName());
			}
		});

		List<Dataset> productsByDesc = new ArrayList<>();

		for(Dataset d : datasetRepository2){
			if(d.getDescription().contains(principal) && !d.isHidden()) {
				productsByDesc.add(d);
			}
		}

		Collections.sort(productsByDesc, new Comparator<Dataset>() {
			@Override
			public int compare(Dataset d1, Dataset d2){
				return d1.getDescription().compareTo(d2.getDescription());
			}
		});

		product.addAll(productsByDesc);

		return product;
	}

	//G0: View the datasets currently on offer (generally)
	@GetMapping(path="/show")
    public String showTable(Model model) {
		List<Dataset> products = datasetRepository.findAll();
		model.addAttribute("products", products);

		return "Offers";
    }

	//O6: Something extra - Review Page
	@GetMapping("/reviews")
	public Object reviewPage(Model model){
		List<Review> reviews = new ArrayList<>();

		for(Review r: reviewRepository.findAll()){
			if(r.getReview() != "") reviews.add(r);
		}
		model.addAttribute("reviews", reviews);

		return "Reviews";
	}
	
	//G1: View details about a particular dataset (generally)
	@GetMapping("/show/{datasetId}")
    public String datasetPage(@PathVariable int datasetId, Model model) {
		Dataset d = datasetRepository.getById(datasetId);

		model.addAttribute("name", d.getName());
		model.addAttribute("desc", d.getDescription());
		model.addAttribute("count", d.getNumberOfDataPoints());
		model.addAttribute("price", d.getPricePerDataPoint());

		return "Product";
    }
}
