package com.sample;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Before;
import org.junit.Test;

import be.cm.apps.playground.util.NanoMeter;

import com.sample.model.Price;
import com.sample.model.Product;
import com.sample.model.PurchaseContext;

 
public class PriceCalculatorTest {
	
	private NanoMeter log = null;
	private KnowledgeBase kbase = null;
	
	@Before
	public void setUp() {
		log = new NanoMeter();
	}
	
	@Test
	public void testNoReduction() {
		Product product = new Product("pencil", new BigDecimal(10));
		Price price = PriceCalculator.calculate(product);
		
		Assert.assertEquals(product.getBasicPrice(), price.getFinalPrice());
		Assert.assertNotNull(price.getReduction());
		Assert.assertEquals( BigDecimal.ZERO  , price.getReduction());
	}
	
	@Test
	public void testRunRules() throws Exception {
		// load up the knowledge base
		kbase = readKnowledgeBase();
		
		log.log("fireAllRules");
		Price price = runRules();
		
		System.out.println( price);
	}
	
	private Price runRules() {
		log.log("creating session");
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

		// go !
		Product product = new Product("pencil", new BigDecimal(10));
		ksession.insert(product);
		Price price = new Price();
		PurchaseContext context = new PurchaseContext();
		Calendar aDate = Calendar.getInstance();
		aDate.set(2011 , Calendar.JANUARY, 1);
		context.setPurchaseDate( aDate );

		ksession.insert(price);
		ksession.insert(context);
		
		ksession.fireAllRules();
		logger.close();
		return price;

	}
	
	
	private  KnowledgeBase readKnowledgeBase() throws Exception {
		log.log("newKnowledgeBuilder");
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		log.log("adding rules");
		kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error: errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		log.log("creating KnowledgeBase");
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

}
