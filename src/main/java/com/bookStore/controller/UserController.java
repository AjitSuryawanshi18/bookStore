	package com.bookStore.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bookStore.entity.MyFav_Books;
import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.BookService;
import com.bookStore.service.MyFav_BooksService;
import com.bookStore.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BookService service;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MyFav_BooksService myBookService;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepository.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@GetMapping("/user_profile")
	public String profile(Model model) {
		
		model.addAttribute("activePage", "profile");

		model.addAttribute("title", "User Profile - BookStore");

		return "/userProfile/user_profile";
	}

	@GetMapping("/user_Home")
	public String user_Home(Model model) {
		model.addAttribute("activePage", "user_Home");

		model.addAttribute("title", "User Home - BookStore");

		return "/userProfile/user_Home";
	}

//	delete user account permanent
//	@GetMapping("/delete-account")
//	public String deleteAccount(@ModelAttribute User user, HttpSession session) {
//
////		 Delete the user
//		int userId = user.getId();
//        userRepository.deleteById(userId);
//		
//		session.setAttribute("msg", "Your Account deleted Succesfully!!");
//		
//		return "redirect:/signin";
//	}
	
	
//	delete user account permanent
	@GetMapping("/delete-account/{userId}")
	public String deleteAccount(@PathVariable("userId") int userId, HttpSession session) {
		
//		 Delete the user
		
		boolean isDeleted = userService.deleteUserAccount(userId);
//		userRepository.deleteById(userId);
		
		if (isDeleted) {
			session.setAttribute("msg", "Your Account deleted Succesfully!!");
			
			return "redirect:/signin";
			
		} else {
			session.setAttribute("msg", "User Not Found!!");
			
			return "/userProfile/user_profile";
		}
		
		
	}
	
	
	
	
	
	

	//this controller for all available books page controller method 
//	@GetMapping("/available_books")
//	public ModelAndView available_books(Model model) {
//		model.addAttribute("activePage", "available_books");
//		model.addAttribute("title", "available_books - bookStore");
//
//		List<book> books = service.getAllBooks();
//		return new ModelAndView("available_books", "books", books);
//	}
	
	
	// available books new category page controller method
	@GetMapping("/available_NewBooks")
	public ModelAndView available_NewBooks(Model model) {
		model.addAttribute("activePage", "available_NewBooks");
		model.addAttribute("title", "available_NewBooks - bookStore");
		
		List<book> books = service.getBooksByType("new");
		return new ModelAndView("available_NewBooks", "books", books);
	}
	
	// available books Resale category page controller method
	@GetMapping("/available_ResaleBooks")
	public ModelAndView available_ResaleBooks(Model model) {
		model.addAttribute("activePage", "available_ResaleBooks");
		model.addAttribute("title", "available_ResaleBooks - bookStore");
		
		List<book> books = service.getBooksByType("resale");
		return new ModelAndView("available_ResaleBooks", "books", books);
	}

	
	
	
	// fav_books page controller method fav_books
	@GetMapping("/fav_books")
	public String fav_books(Model model) {
		model.addAttribute("activePage", "fav_books");
		model.addAttribute("title", "fav_books - bookStore");

		// for adding data to the favBooks after clicking add to Fav Books it will shows
		// data is added or not on UI
		List<MyFav_Books> list = myBookService.getAllMyBooks();
		model.addAttribute("book", list);

		return "/fav_books";
	}

	// first mapping for change password
	@GetMapping("/change_PasswordUser")
	public String change_PasswordUser() {

		return "/userProfile/change_PasswordUser";
	}

//			second mapping for change password for admin module after submitting data on the form
	@PostMapping("/change_PasswordUser-Post")
	public String change_Password(@ModelAttribute User user, HttpSession session,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword) {

		// accept data and process it for change password

//				System.out.println(oldPassword);
//				System.out.println(newPassword);
//				System.out.println(confirmPassword);

//				bcrypted password from database will be matches with new password after encoding

//				System.out.println(bCryptPasswordEncoder.matches(oldPassword, user.getPassword()));

		if (passwordEncoder.matches(oldPassword, user.getPassword())) {
//					System.out.println("old password Matches...");			
			if (newPassword.equals(confirmPassword)) {

				// write save password to the database logic after changing password

				String password = passwordEncoder.encode(confirmPassword);
				user.setPassword(password);

				User u = userService.saveUser(user);

				if (u != null) {

					session.setAttribute("msg", "Password Changed Successfully");

				} else {

					session.setAttribute("msg", "Something went wrong");
				}
//							System.out.println("your Password save successfully...!");

			} else {
				session.setAttribute("msg_red", "Confirm Password Should Be Match With New Password..!");

//						System.out.println("new password and confirm password should be matches..!");
			}

		} else {
			session.setAttribute("msg_red", "Please Enter Correct Old Password..!");

//					System.out.println("Please enter Correct old PAssword..!");
		}

		return "redirect:/user/change_PasswordUser";
	}

//			upload profile photo mapping methods for user start

//			profile photo upload mapping
	@PostMapping("/uploadProfileImage")
	public String uploadProfileImage(@RequestParam("file") MultipartFile profileimageFile,
			@ModelAttribute User userProfImage, Model model, Principal p) {
//		        userService.uploadProfileImage(profileimageFile);
//				System.out.println("image selected original " + profileimageFile.getOriginalFilename());

		try {
			if (!profileimageFile.isEmpty()) {

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timestamp = dateFormat.format(new Date());

				String fileName = profileimageFile.getOriginalFilename();
				String newFileName = timestamp + fileName;
				String directoryPath = "C:\\Users\\ajits\\Documents\\workspace-spring-tool-suite-4-4.19.0.RELEASE\\BOOKSTORE_PROJECT\\bookStore/src/main/resources/static/images/UserProfile_Photos/";
//						String directoryPath = "";

				// Create the directory if it doesn't exist
				File directory = new File(directoryPath);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				String filePath = directoryPath + newFileName;
				Files.copy(profileimageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

				// modified name saved in the database

				userProfImage.setProfileImage(newFileName); // actually image name
				userService.saveUser(userProfImage);
//						System.out.println("image selected after new name generation " + userProfImage.getProfileImage());

			}

		} catch (IOException e) {
			// Handle the exception appropriately
			e.printStackTrace();

			// Provide a user-friendly message
			model.addAttribute("error", "Error uploading the image. Please try again.");

		}

		return "/userProfile/user_profile";
	}

//			serving image to profile photo to the user login

//			serving image to the html pages

	private static final String IMAGE_FOLDER1 = "C:\\Users\\ajits\\Documents\\workspace-spring-tool-suite-4-4.19.0.RELEASE\\BOOKSTORE_PROJECT\\bookStore\\src\\main\\resources\\static\\images\\UserProfile_Photos/";

	@GetMapping("/images")
	public ResponseEntity<Resource> serveImageToProfilePhoto(@ModelAttribute User user)
			throws FileNotFoundException, IOException {

//				System.out.println(user.getProfileImage());
		String profileImageName = user.getProfileImage();

//				System.out.println("image name while serving" + profileImageName);

		// it is also ok --> Path imagePath = Paths.get(IMAGE_FOLDER1 +
		// profileImageName);
		Path imagePath = Paths.get(IMAGE_FOLDER1, profileImageName);
		Resource resource;
//				System.out.println("image path while serving" + imagePath);

		try {

			resource = new FileSystemResource(imagePath.toAbsolutePath().toString());

			if (resource.exists()) {
				// if file present in resource it will server to the profile
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE) // MediaType.IMAGE_JPEG_VALUE
						.body(resource);
			} else {
				throw new RuntimeException("Image not found: " + profileImageName);
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to load image: " + e.getMessage());
		}

	}

//			upload profile photo mapping methods for user end
	
	


	
	
}
