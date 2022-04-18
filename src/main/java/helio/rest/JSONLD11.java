package helio.rest;

import java.io.ByteArrayInputStream;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.rdf.RdfLiteral;
import com.apicatalog.rdf.RdfNQuad;
import com.apicatalog.rdf.RdfValue;

import helio.blueprints.exceptions.IncompatibleMappingException;

public class JSONLD11 {

	private static Logger logger = LoggerFactory.getLogger(JSONLD11.class);


	public static Model loadIntoModel(String content) throws IncompatibleMappingException {
		Model model = ModelFactory.createDefaultModel();
		try {
			Document document = JsonDocument.of(new ByteArrayInputStream(content.getBytes()));
			JsonLd.toRdf(document).get().toList().stream()
				.forEach(elem -> model.add(toTriple(elem)));
		} catch (JsonLdError e) {
			System.out.println(">"+content);
			e.printStackTrace();
			throw new IncompatibleMappingException(e.toString());
		}
		return model;
	}

	public static Model toTriple(RdfNQuad quadTriple) {
		Model model = ModelFactory.createDefaultModel();

		try {
		Resource subject = ResourceFactory.createResource(quadTriple.getSubject().toString());
		Property predicate =  ResourceFactory.createProperty(quadTriple.getPredicate().toString());
		RdfValue objectRaw = quadTriple.getObject();

		if(objectRaw.isIRI() || objectRaw.isBlankNode()) {
			Resource object =  ResourceFactory.createResource(quadTriple.getObject().getValue());
			model.add(subject, predicate, object);
		}else {
			RdfLiteral literal = objectRaw.asLiteral();
			Literal jenaLiteral = ResourceFactory.createPlainLiteral(literal.getValue());
			if(literal.getLanguage().isPresent()) {
				jenaLiteral = ResourceFactory.createLangLiteral(literal.getValue(), literal.getLanguage().get());
			}else if(literal.getDatatype()!=null && !literal.getDatatype().isEmpty()) {
				jenaLiteral = ResourceFactory.createTypedLiteral(literal.getValue(), new BaseDatatype(literal.getDatatype()));
			}
			model.add(subject, predicate, jenaLiteral);
		}
		}catch(Exception e) {
			logger.error("Error adding tirple : "+quadTriple);
			logger.error(e.toString());
		}

		return model;
	}
}
