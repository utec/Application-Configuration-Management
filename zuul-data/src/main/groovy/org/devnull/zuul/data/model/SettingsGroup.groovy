package org.devnull.zuul.data.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import org.devnull.zuul.data.config.ZuulDataConstants
import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import org.codehaus.jackson.annotate.JsonBackReference
import org.codehaus.jackson.annotate.JsonIgnore
import javax.persistence.CascadeType

@Entity
@EqualsAndHashCode(excludes = 'entries')
@ToString(includeNames = true, excludes = 'entries')
class SettingsGroup implements Serializable {

    static final long serialVersionUID = ZuulDataConstants.API_VERSION

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @OneToMany(mappedBy = "group", cascade=[CascadeType.ALL])
    List<SettingsEntry> entries = []

    @ManyToOne
    @JoinColumn(name="environment")
    @JsonBackReference
    Environment environment

    @ManyToOne
    @JoinColumn(name="key")
    @JsonIgnore
    EncryptionKey key

    String name

    void addToEntries(SettingsEntry entry) {
        entry.group = this
        entries << entry
    }

    def asType(Class type) {
        switch (type) {
            case Properties:
                def properties = new Properties()
                entries.each {
                    properties.put(it.key, it.value)
                }
                return properties
            break
            default:
                throw new GroovyCastException("Hmm... ${this.class} cannot be converted to ${type}")
        }
    }
}
