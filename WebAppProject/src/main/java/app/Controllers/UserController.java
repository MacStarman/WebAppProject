package app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import app.Entities.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping(path="/User")
public class UserController {
	@Autowired
	private app.JpaRepository.UserRepository userRepository;

	@Autowired
	private app.JpaRepository.DatasetRepository datasetRepository;

	@Autowired
	private app.JpaRepository.OrdersRepository orderRepository;

	@GetMapping(path = "/all")
	public @ResponseBody
	Iterable<app.Entities.User> getAllUsers() {
		userRepository.findAll();
		return userRepository.findAll();
	}

	//Use home screen
	@GetMapping(path = "/{id}")
	public String homePage(@PathVariable int id, Model model) {
		model.addAttribute("id", id);

		return "UserHome";
	}

	//C3: Something extra - search engine - User
	@GetMapping("/{id}/search")
	public String search(@RequestParam String principal, @PathVariable int id, Model model) {
		List<Dataset> products = new ArrayList<>();
		model.addAttribute("id", id);
		model.addAttribute("products", filterVisibility(principal, products));
		return "UserSearch";
	}

	//C3: Something extra - helper method
	private List<Dataset> filterVisibility(String principal, List<Dataset> product) {
		List<Dataset> datasetRepository2 = new ArrayList<>();

		for (Dataset d : datasetRepository.findAll()) {
			datasetRepository2.add(d);
		}

		for (Dataset d : datasetRepository2) {
			if (d.getName().contains(principal) && !d.isHidden()) {
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
			if (d.getDescription().contains(principal) && !d.isHidden()) {
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

	//G0: View the datasets currently on offer (as a user)
	@GetMapping(path = "/{id}/show")
	public String showTable(@PathVariable int id, Model model) {
		List<Dataset> products = datasetRepository.findAll();
		model.addAttribute("products", products);
		model.addAttribute("id", id);

		return "UserOffers";
	}

	//G1: View details about a particular dataset (as a user)
	//Button to add datasets to cart only appears for user with login
	@GetMapping("/{userId}/show/{datasetId}")
	public String datasetPage(@PathVariable int userId, @PathVariable int datasetId, Model model) {
		Dataset d = datasetRepository.getById(datasetId);

		model.addAttribute("datasetId", datasetId);
		model.addAttribute("name", d.getName());
		model.addAttribute("desc", d.getDescription());
		model.addAttribute("count", d.getNumberOfDataPoints());
		model.addAttribute("price", d.getPricePerDataPoint());
		model.addAttribute("id", userId);

		return "UserProduct";
	}

	//G2: add a set of datapoints to a shopping cart
	//A user can add a certain section of rows
	@GetMapping("/{userId}/add")
	public @ResponseBody
	String add(@PathVariable int userId, @RequestParam int datasetId, @RequestParam int newCount) {
		User u = userRepository.getById(userId);
		Dataset d = datasetRepository.getById(datasetId);
		Timestamp now = new Timestamp(System.currentTimeMillis());

		int cartId = (int) (orderRepository.count() + 1);
		Orders o = new Orders(cartId, userId, datasetId, newCount, d.getPricePerDataPoint(), 0);
		o.setCreatedAt(now);
		o.setUpdatedAt(now);
		o.setDatasetName(d.getName());
		orderRepository.save(o);
		String head = "<head>\n" +
				"    <title>\n" +
				"        Project | Checkout Details | C0\n" +
				"    </title>\n" +
				"    <meta charset=\"UTF-8\">\n" +
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
				"    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/style_checkout.css\">\n" +
				"    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
				"    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\"></script>\n" +
				"    <style>\n" +
				"        table {\n" +
				"            font-family: arial,sans-serif;\n" +
				"            border-collapse: collapse;\n" +
				"            width: 100%\n" +
				"        }\n" +
				"        td, th {\n" +
				"            border: 1px solid #dddddd;\n" +
				"            text-align: left;\n" +
				"            padding: 8px;\n" +
				"        }\n" +
				"        tr:nth-child(even) {\n" +
				"            background-color: #dddddd;\n" +
				"        }\n" +
				"    </style>\n" +
				"</head>\n" +
				"<body>\n" +
				"\n" +
				"<div class=\"container-fluid p-5 bg-primary text-white text-center\">\n" +
				"    <h1>Randomly assigned market place</h1>\n" +
				"    <p>Best Random deals around</p>\n" +
				"</div>\n" +
				"\n" +
				"<nav class=\"navbar navbar-expand-sm navbar-dark bg-dark\">\n" +
				"    <div class=\"container-fluid\">\n" +
				"        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#mynavbar\">\n" +
				"            <span class=\"navbar-toggler-icon\"></span>\n" +
				"        </button>\n" +
				"        <div class=\"collapse navbar-collapse\" id=\"mynavbar\">\n" +
				"            <ul class=\"navbar-nav me-auto\">\n" +
				"                <li class=\"nav-item\">\n" +
				"                    <a class=\"nav-link\" href=\"/User/" + userId + "\">Home</a>\n" +
				"                </li>\n" +
				"                <li class=\"nav-item\">\n" +
				"                    <a class=\"nav-link\" href=\"/show\">Offers</a>\n" +
				"                </li>\n" +
				"                <li class=\"nav-item\">\n" +
				"                    <a class=\"nav-link\" href=\"Orders.html\">Orders</a>\n" +
				"                </li>\n" +
				"                <li class=\"nav-item\">\n" +
				"                    <a class=\"nav-link\" href=\"/\">Log Out</a>\n" +
				"                </li>\n" +
				"                <li class=\"nav-item\">\n" +
				"                    <a class=\"nav-link\" href=\"Cart.html\">Shopping Cart</a>\n" +
				"                </li>\n" +
				"            </ul>\n" +
				"            <form class=\"d-flex\">\n" +
				"                <input class=\"form-control me-2\" type=\"text\" placeholder=\"Search\">\n" +
				"                <button class=\"btn btn-primary btn-sm\" type=\"button\">Search</button>\n" +
				"            </form>\n" +
				"        </div>\n" +
				"    </div>\n" +
				"</nav>";
		return head + "<h2>" + newCount + " were added to " + u.getName() + "'s shopping cart.</h2>"
				+ "<meta http-equiv=\"refresh\" content=\"1; url=/User/" + userId + "/show\">";
	}

	//G2 continued: Basic implementation of user-specific shopping cart
	@GetMapping("/{userId}/shoppingcart")
	public String viewShoppingCart(@PathVariable int userId, Model model) {
		List<Orders> orders = new ArrayList<>();
		float price = 0;

		for (Orders o : orderRepository.findAll()) {
			if (o.getUser() == userId && o.getStatus() == 0) {
				orders.add(o);
				o.setDatasetName(datasetRepository.getById(o.getDataset()).getName());
				price += o.getPrice();
			}
		}

		model.addAttribute("price", price);
		model.addAttribute("orders", orders);
		model.addAttribute("id", userId);
		model.addAttribute("note", orderRepository.getNotes().get(userId));

		return "Cart";
	}
}