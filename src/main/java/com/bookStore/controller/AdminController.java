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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bookStore.entity.User;
import com.bookStore.entity.book;
import com.bookStore.repository.BookRepository;
import com.bookStore.repository.UserRepository;
import com.bookStore.service.BookService;
//import com.bookStore.service.RegNewBookService;
import com.bookStore.service.UserService;

import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	BookService service;

//	@Autowired
//	RegNewBookService regNewBookService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User adminUser = userRepository.findByEmail(email);
			m.addAttribute("user", adminUser);
		}
	}

	@GetMapping("/admin_Home")
	public String home(Model model) {
		
		model.addAttribute("activePage", "admin_Home");
		return "/adminProfile/admin_Home";
	}

	@GetMapping("/admin_profile")
	public String profile(Model model) {
		
		model.addAttribute("activePage", "admin_profile");
		return "adminProfile/admin_profile";
	}
	
	
	
	
	
//	profile photo upload mapping
	@PostMapping("/uploadProfileImage")
	public String uploadProfileImage(@RequestParam("file") MultipartFile profileimageFile,
			@ModelAttribute User userProfImage, Model model, Principal p) {
//        userService.uploadProfileImage(profileimageFile);
//		System.out.println("image selected original " + profileimageFile.getOriginalFilename());

		try {
			if (!profileimageFile.isEmpty()) {

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timestamp = dateFormat.format(new Date());

				String fileName = profileimageFile.getOriginalFilename();
				String newFileName = timestamp + fileName;
				String directoryPath = "C:\\Users\\ajits\\Documents\\workspace-spring-tool-suite-4-4.19.0.RELEASE\\BOOKSTORE_PROJECT\\bookStore/src/main/resources/static/images/UserProfile_Photos/";
//				String directoryPath = "";

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
//				System.out.println("image selected after new name generation " + userProfImage.getProfileImage());

			}

		} catch (IOException e) {
			// Handle the exception appropriately
			e.printStackTrace();

			// Provide a user-friendly message
			model.addAttribute("error", "Error uploading the image. Please try again.");

		}

		return "adminProfile/admin_profile";
	}

	
	
	
	
//	serving image to profile photo admin login

//	serving image to the html pages

	private static final String IMAGE_FOLDER1 = "C:\\Users\\ajits\\Documents\\workspace-spring-tool-suite-4-4.19.0.RELEASE\\BOOKSTORE_PROJECT\\bookStore\\src\\main\\resources\\static\\images\\UserProfile_Photos/";

	@GetMapping("/images")
	public ResponseEntity<Resource> serveImageToProfilePhoto(@ModelAttribute User user) throws FileNotFoundException, IOException {

//		System.out.println(user.getProfileImage());
		String profileImageName=user.getProfileImage();

//		System.out.println("image name while serving" + profileImageName);

//it is also ok -->		Path imagePath = Paths.get(IMAGE_FOLDER1 + profileImageName);
		Path imagePath = Paths.get(IMAGE_FOLDER1, profileImageName);
		Resource resource;
//		System.out.println("image path while serving" + imagePath);
		

		try {
		
             resource = new FileSystemResource(imagePath.toAbsolutePath().toString());

             if (resource.exists()) {
            	 //if file present in resource it will server to the profile
            	 return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE) // MediaType.IMAGE_JPEG_VALUE
         				.body(resource);
             } else {
                 throw new RuntimeException("Image not found: " + profileImageName);
             }
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to load image: " + e.getMessage());
		}

		
	}

	
	
	
	
	
	
	// register new book controller method
	@GetMapping("/register_newbook")
	public String register_newbook(Model model) {
		model.addAttribute("activePage", "register_newbook");
		model.addAttribute("title", "register_newbook - bookStore");

		return "/adminProfile/register_newbook";
	}

	// controller for addBook or save book from admin login request come here to
	// store new registered book
	@PostMapping("/saveBook")
	public String addBook(@ModelAttribute book b, @RequestParam MultipartFile img, Model model, HttpSession session) {

		// work on image upload here

		System.out.println(img.getOriginalFilename());

//		book newBook = bookRepository.save(b);

//		trying new way to manage image data

		try {
			if (!img.isEmpty()) {

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timestamp = dateFormat.format(new Date());

				String fileName = img.getOriginalFilename();
				String newFileName = timestamp + fileName;
				String directoryPath = "C:\\Users\\ajits\\Documents\\workspace-spring-tool-suite-4-4.19.0.RELEASE\\BOOKSTORE_PROJECT\\bookStore\\src\\main\\resources\\static\\images/";

				// Create the directory if it doesn't exist
				File directory = new File(directoryPath);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				String filePath = directoryPath + newFileName;
				Files.copy(img.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

				// modified name saved in the database
				b.setbookImageName(newFileName);
			}
			// book saves in database
			book newBook = service.saveBook(b);
			
//			System.out.println("Book Type : "+newBook.getBookType());

//			System.out.println(newBook.getBookImageName());
//	           System.out.println(newBook.getBookDescription());;

			// display proper message after book save operation
			if (newBook != null) {

				session.setAttribute("msg", "New Book Registered successfully");

			}

//			this code is not required or dead code because if any error or exception occurs it will execute catch 
//          if(newBook ==null) {
//
//				session.setAttribute("msg", "Something went wrong");
//			}

		} catch (IOException e) {
			// Handle the exception appropriately
			e.printStackTrace();

			// Provide a user-friendly message
			model.addAttribute("error", "Error uploading the image or saving new book. Please try again.");

		}

		return "redirect:/admin/register_newbook";
	}

	// controller for UpdateBook from admin login request come here to store updated
	// book
	@PostMapping("/updateBook/{id}")
	public String updateBook(@ModelAttribute book updatedBook,@PathVariable("id") int id, Model model) {
		
		//fetching Existing book from database to set newly updated values
		book existingBook =service.getBookById(id);
//		
//		String BookDescription=existingBook.getBookDescription();
//		String BookType=existingBook.getBookType();
//		String BookImageName=existingBook.getBookImageName();
//		
		 // Update only the fields that are provided in the updatedBook
        if (updatedBook.getName() != null) {
            existingBook.setName(updatedBook.getName());
        }
        if (updatedBook.getAuthor() != null) {
            existingBook.setAuthor(updatedBook.getAuthor());
        }
        if (updatedBook.getPrice() != null) {
        	existingBook.setPrice(updatedBook.getPrice());
        }
        
		
//		regNewBookService.saveNewBook(b);
        //After setting new values to the existing book values now we can save Existing book again to the database.
		service.saveBook(existingBook);

		return "redirect:/admin/available_books";
	}

//	work on image show with other book details

	@GetMapping("/available_books")
	public String available_books(Model model) {
		model.addAttribute("activePage", "available_books");
		model.addAttribute("title", "available_books - bookStore");

//		List<book> list = service.available_books();
//		List<book> list = bookRepository.findAll();

		List<book> books = service.getAllBooks();
		model.addAttribute("books", books);

//		model.addAttribute("books", list);

		return "available_books";
	}

//	serving image to the html pages

	private static final String IMAGE_FOLDER = "C:\\Users\\ajits\\Documents\\workspace-spring-tool-suite-4-4.19.0.RELEASE\\BOOKSTORE_PROJECT\\bookStore\\src\\main\\resources\\static\\images/";

	@GetMapping("/images/{imageName}")
	public ResponseEntity<Resource> serveImage(@PathVariable String imageName)
			throws FileNotFoundException, IOException {

		System.out.println("image name while serving" + imageName);

		Path imagePath = Paths.get(IMAGE_FOLDER + imageName);
		Resource resource;

		try (InputStream inputStream = new FileInputStream(imagePath.toFile())) {
			resource = (Resource) new InputStreamResource(inputStream);

		} catch (MalformedURLException e) {
			throw new RuntimeException("Failed to load image: " + e.getMessage());
		}

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE) // MediaType.IMAGE_JPEG_VALUE
				.body(resource);
	}


//		controller for delete book
	@GetMapping("/deleteBook/{id}")
	public String deleteBook(@PathVariable("id") int id) {
		service.deleteById(id);
		return "redirect:/admin/available_books";
	}

	// controller for edit book
	@GetMapping("/editBook/{id}")
	public String editBook(@PathVariable("id") int id, Model model) {
		book b = service.getBookById(id);
		model.addAttribute("book", b);
		return "adminProfile/editBook";
	}

	// first mapping for change password
	@GetMapping("/change_PasswordAdmin")
	public String change_PasswordAdmin() {

		return "/adminProfile/change_PasswordAdmin";
	}

//	second mapping for change password for admin module after submitting data on the form
	@PostMapping("/change_PasswordAdmin-Post")
	public String change_Password(@ModelAttribute User user, HttpSession session,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword) {

		// accept data and process it for change password

//		System.out.println(oldPassword);
//		System.out.println(newPassword);
//		System.out.println(confirmPassword);

//		bcrypted password from database will be matches with new password after encoding

//		System.out.println(bCryptPasswordEncoder.matches(oldPassword, user.getPassword()));

		if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
//			System.out.println("old password Matches...");			
			if (newPassword.equals(confirmPassword)) {

				// write save password to the database logic after changing password

				String password = bCryptPasswordEncoder.encode(confirmPassword);
				user.setPassword(password);

				User u = userService.saveUser(user);

				if (u != null) {

					session.setAttribute("msg", "Password Changed Successfully");

				} else {

					session.setAttribute("msg", "Something went wrong");
				}
//					System.out.println("your Password save successfully...!");

			} else {
				session.setAttribute("msg_red", "Confirm Password Should Be Match With New Password..!");

//				System.out.println("new password and confirm password should be matches..!");
			}

		} else {
			session.setAttribute("msg_red", "Please Enter Correct Old Password..!");

//			System.out.println("Please enter Correct old PAssword..!");
		}

		return "redirect:/admin/change_PasswordAdmin";
	}

}
