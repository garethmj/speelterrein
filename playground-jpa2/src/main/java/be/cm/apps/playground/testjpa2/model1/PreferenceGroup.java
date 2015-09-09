package be.cm.apps.playground.testjpa2.model1;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static org.eclipse.persistence.annotations.JoinFetchType.INNER;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.PrivateOwned;

/**
 * Entity that demonstrates the use of a jointable in a unidirectional OneToMany relation.
 *
 */
@Entity
@SequenceGenerator(name = "PreferenceGroupSequence")
public class PreferenceGroup implements Serializable {

	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PreferenceGroupSequence")
	@Id
	private long id;
	
	private String name;
	private String version;   
	private static final long serialVersionUID = 1L;
	
	@JoinTable(name = "PREFJOINTABLE", inverseJoinColumns = @JoinColumn(name = "ID"), joinColumns = @JoinColumn(name = "Configuration_id", referencedColumnName = "id"))
	@OneToMany
	@PrivateOwned
	private List<Parameter> parameters;

	public PreferenceGroup() {
		super();
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
   
}
