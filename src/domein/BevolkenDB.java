package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.ProductInformationRequiredException;
import repository.BedrijfDaoJpa;
import repository.BestellingDaoJpa;
import repository.BestellingProductDaoJpa;
import repository.GenericDaoJpa;
import repository.ProductDaoJpa;

public class BevolkenDB {
	public void run() {
		BedrijfDaoJpa.startTransaction();

		BedrijfDaoJpa bedrijfDao = new BedrijfDaoJpa();
		Bedrijf bd = new Bedrijf("https://cdn.pixabay.com/photo/2014/06/03/19/38/road-sign-361514_960_720.png",
				"Bedrijf1", "Sector1", "Straat 1, 1000 Brussel br, België",
				Arrays.asList( BetalingsMethodesEnum.BANCONTACT,BetalingsMethodesEnum.FACTUUR, BetalingsMethodesEnum.PAYPAL),
				// temporary email receiver online app.
				Map.of("contactpersoon", "Bedrijf1 Contactpersoon", "email", "ht5ut3sy@temporary-mail.net", "telefoon",
						"+3212345678901"),
				"BE0123456789", LocalDate.of(2023, 11, 1), true, false);
		Bedrijf bd2 = new Bedrijf(
				"https://cdn.pixabay.com/photo/2014/06/03/19/38/road-sign-361514_960_720.png", "Bedrijf2", "Sector2",
				"Straat 32, 9000 Gent, België", Arrays.asList(BetalingsMethodesEnum.PAYPAL), Map.of("contactpersoon",
						"Bedrijf2 Contactpersoon", "email", "info@bedrijf2.com", "telefoon", "+3212363278901"),
				"BE0555555555", LocalDate.of(2024, 1, 12), false, true);

		Leverancier leverancier1 = new Leverancier("leverancier1", "testww1");
		Leverancier leverancier2 = new Leverancier("leverancier2", "testww2");
		Klant klant1 = new Klant("klant1", "testww1");
		Klant klant2 = new Klant("klant2", "testww2");

		bd.setLeverancier(leverancier1);
		bd.setKlant(klant1);
		bd2.setLeverancier(leverancier2);
		bd2.setKlant(klant2);

		bedrijfDao.insert(bd);
		bedrijfDao.insert(bd2);
		BedrijfDaoJpa.commitTransaction();

		GenericDaoJpa<Admin> adminDao = new GenericDaoJpa<>(Admin.class);

		adminDao.insert(new Admin("a1", "pw1"));
		adminDao.insert(new Admin("a2", "pw2"));

		GenericDaoJpa<Klant> klantDao = new GenericDaoJpa<>(Klant.class);

		klant1.setBedrijf(bd);
		bd.setKlant(klant1);
		klant2.setBedrijf(bd2);
		klantDao.insert(klant1);
		klantDao.insert(klant2);

		GenericDaoJpa<Leverancier> leverancierDao = new GenericDaoJpa<>(Leverancier.class);

		leverancier1.setBedrijf(bd);
		leverancier2.setBedrijf(bd2);

		leverancier1.setKlanten(List.of(klant2));
		leverancier2.setKlanten(List.of(klant1));

		leverancierDao.insert(leverancier1);
		leverancierDao.insert(leverancier2);

		ProductDaoJpa.startTransaction();
		ProductDaoJpa productDao = new ProductDaoJpa();
		Product product1 = null;
		Product product2 = null;
		Product product3 = null;
		try {
			product1 = new Product.ProductBuilder().naam("Laptop").inStock(10).eenheidsprijs(800.0).fotoUrl(
					"https://cdn-dynmedia-1.microsoft.com/is/image/microsoftcorp/PDP-Highlight-Consumer-Laptop-Go-3-Platinum-001:VP1-539x440")
					.leverancier(leverancier2).build();
			product2 = new Product.ProductBuilder().naam("Smartphone").inStock(20).eenheidsprijs(600.0).fotoUrl(
					"https://cdn-dynmedia-1.microsoft.com/is/image/microsoftcorp/PDP-Highlight-Consumer-Laptop-Go-3-Platinum-001:VP1-539x440")
					.leverancier(leverancier1).build();
			product3 = new Product.ProductBuilder().naam("Tablet").inStock(15).eenheidsprijs(400.0).fotoUrl(
					"https://cdn-dynmedia-1.microsoft.com/is/image/microsoftcorp/PDP-Highlight-Consumer-Laptop-Go-3-Platinum-001:VP1-539x440")
					.leverancier(leverancier1).build();
			productDao.insert(product1);
			productDao.insert(product2);
			productDao.insert(product3);

			leverancier1.setProducten(List.of(product1, product3));
		} catch (ProductInformationRequiredException e) {
			System.out.println(e.getMessage());
		}

		ProductDaoJpa.commitTransaction();

		BestellingDaoJpa.startTransaction();

		Bestelling bestelling1 = new Bestelling("ORD123", LocalDate.now(), "123 Elm St",
				LeveringStatusEnum.INBEHANDELING, BetalingsStatusEnum.NIETBETAALD, klant2, leverancier1,
				LocalDate.now(), 1800);

		Bestelling bestelling2 = new Bestelling("ORD456", LocalDate.now(), "456 Pine St", LeveringStatusEnum.GELEVERD,
				BetalingsStatusEnum.BETAALD, klant1, leverancier2, LocalDate.now().plusDays(30),1000);

		Bestelling bestelling3 = new Bestelling("ORD789", LocalDate.now(), "789 Maple St", LeveringStatusEnum.VERZONDEN,
				BetalingsStatusEnum.BETAALD, klant2, leverancier1, LocalDate.now(),400);

		Bestelling bestelling4 = new Bestelling("ORD23", LocalDate.now(), "501 Joske St", LeveringStatusEnum.GELEVERD,
				BetalingsStatusEnum.NIETBETAALD, klant2, leverancier1, LocalDate.now().plusDays(30),400);

		Bestelling bestelling5 = new Bestelling("ORD13", LocalDate.now(), "12 Jef St", LeveringStatusEnum.GELEVERD,
				BetalingsStatusEnum.BETAALD, klant1, leverancier2, LocalDate.now().plusDays(30),400);

		Bestelling bestelling6 = new Bestelling("ORD251", LocalDate.now(), "3 Xander St",
				LeveringStatusEnum.INBEHANDELING, BetalingsStatusEnum.BETAALD, klant1, leverancier2,
				LocalDate.now().plusDays(30),400);

		BestellingDaoJpa bestellingDao = new BestellingDaoJpa();
		bestellingDao.insert(bestelling1);
		bestellingDao.insert(bestelling2);

		bestellingDao.insert(bestelling3);

		bestellingDao.insert(bestelling4);

		bestellingDao.insert(bestelling5);

		bestellingDao.insert(bestelling6);

		BestellingDaoJpa.commitTransaction();

		//BestellingProduct bestellingProduct1 = new BestellingProduct(bestelling1, product1, 800.0, 2);
		BestellingProduct bestellingProduct2 = new BestellingProduct(bestelling1, product2, 600.0, 5);
		BestellingProduct bestellingProduct3 = new BestellingProduct(bestelling1, product3, 400.0, 3);
		BestellingProduct bestellingProduct4 = new BestellingProduct(bestelling2, product1, 600.0, 5);
		//BestellingProduct bestellingProduct5 = new BestellingProduct(bestelling2, product3, 400.0, 3);

		BestellingProduct bestellingProduct6 = new BestellingProduct(bestelling3, product3, 400.0, 3);
		BestellingProduct bestellingProduct7 = new BestellingProduct(bestelling4, product3, 400.0, 3);
		BestellingProduct bestellingProduct8 = new BestellingProduct(bestelling5, product1, 400.0, 3);
		BestellingProduct bestellingProduct9 = new BestellingProduct(bestelling6, product1, 400.0, 3);
		
		BestellingProductDaoJpa.startTransaction();
		BestellingProductDaoJpa bestellingProductJpa = new BestellingProductDaoJpa();
		//bestellingProductJpa.insert(bestellingProduct1);
		bestellingProductJpa.insert(bestellingProduct2);
		bestellingProductJpa.insert(bestellingProduct3);
		bestellingProductJpa.insert(bestellingProduct4);
		//bestellingProductJpa.insert(bestellingProduct5);
		bestellingProductJpa.insert(bestellingProduct6);
		bestellingProductJpa.insert(bestellingProduct7);
		bestellingProductJpa.insert(bestellingProduct8);
		bestellingProductJpa.insert(bestellingProduct9);
		BestellingProductDaoJpa.commitTransaction();

		BestellingDaoJpa.startTransaction();
		bestelling1.setBestellingProducts(List.of(/*bestellingProduct1,*/ bestellingProduct2, bestellingProduct3));
		bestelling2.setBestellingProducts(List.of(bestellingProduct4/*, bestellingProduct5*/));
		bestelling3.setBestellingProducts(List.of(bestellingProduct6));
		bestelling4.setBestellingProducts(List.of(bestellingProduct7));
		bestelling5.setBestellingProducts(List.of(bestellingProduct8));
		bestelling6.setBestellingProducts(List.of(bestellingProduct9));
		
		bestelling1.berekenBedrag();
		bestelling2.berekenBedrag();
		bestelling3.berekenBedrag();
		bestelling4.berekenBedrag();
		bestelling5.berekenBedrag();
		bestelling6.berekenBedrag();
		
		BestellingDaoJpa.commitTransaction();

		BedrijfVerandering bedrijfVerandering = new BedrijfVerandering();
		bedrijfVerandering.setBedrijf(bd);
		bedrijfVerandering.setLogo("https://cdn.pixabay.com/photo/2014/06/03/19/38/road-sign-361514_960_720.png");
		bedrijfVerandering.setSector("Mock Sector");
		bedrijfVerandering.setAdres("MockStraat 1, 1000 Haasdonk, België");
		bedrijfVerandering.setAccountSinds(LocalDate.of(2022, 1, 1));
		bedrijfVerandering.setActiefKlant(true);
		bedrijfVerandering.setActiefLeverancier(false);
		bedrijfVerandering.setAangevraagdOp(LocalDateTime.now());
		bedrijfVerandering.setAangevraagdDoor((IUser) leverancier1);
		Map<String, String> contactGegevens = new HashMap<>();
		contactGegevens.put("email", "mock@example.com");
		contactGegevens.put("telefoon", "+3212363232101");
		contactGegevens.put("contactpersoon", "Jefke ");
		bedrijfVerandering.setContactGegevens(contactGegevens);
		bedrijfVerandering.setGoedgekeurd(false);
		bedrijfVerandering.setAfgekeurd(false);
		bedrijfVerandering.setBetalingsInfo(List.of(BetalingsMethodesEnum.FACTUUR, BetalingsMethodesEnum.BANCONTACT, BetalingsMethodesEnum.OVERSCHRIJVING));

		BedrijfVerandering bedrijfVerandering2 = new BedrijfVerandering();
		bedrijfVerandering2.setBedrijf(bd);
		bedrijfVerandering2.setLogo("https://cdn.pixabay.com/photo/2014/06/03/19/38/road-sign-361514_960_720.png");
		bedrijfVerandering2.setSector("Another Mock Sector");
		bedrijfVerandering2.setAdres("Another Mock Address");
		bedrijfVerandering2.setAfgekeurd(false);
		bedrijfVerandering2.setAccountSinds(LocalDate.of(2022, 1, 1));
		bedrijfVerandering2.setActiefKlant(false);
		bedrijfVerandering2.setActiefLeverancier(true);
		bedrijfVerandering2.setAangevraagdOp(LocalDateTime.now());
		bedrijfVerandering2.setAangevraagdDoor((IUser) klant1);
		Map<String, String> contactGegevens2 = new HashMap<>();
		contactGegevens2.put("email", "another_mock@example.com");
		contactGegevens2.put("telefoon", "+987654321");
		contactGegevens2.put("contactpersoon", "Jefke ");
		bedrijfVerandering2.setContactGegevens(contactGegevens2);
		bedrijfVerandering2
				.setBetalingsInfo(Arrays.asList(BetalingsMethodesEnum.PAYPAL, BetalingsMethodesEnum.FACTUUR));
		bedrijfVerandering2.setGoedgekeurd(false);

		BedrijfDaoJpa.startTransaction();
		bd.addChange(bedrijfVerandering);
		bd2.addChange(bedrijfVerandering2);
		BedrijfDaoJpa.commitTransaction();

	}
}