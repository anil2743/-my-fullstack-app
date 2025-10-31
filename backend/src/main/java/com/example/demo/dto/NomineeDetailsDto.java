package com.example.demo.dto;

import java.util.List;

public class NomineeDetailsDto {
    private List<NomineeDto> nominees;
    
    // Getters and Setters
    public List<NomineeDto> getNominees() { return nominees; }
    public void setNominees(List<NomineeDto> nominees) { this.nominees = nominees; }
    
    public static class NomineeDto {
        private Integer id;
        private String firstName;
        private String lastName;
        private String relationship;
        private String dateOfBirth;
        private Integer percentageShare;
        private String minorNomineeFlag;
        private String guardianName;
        private String guardianRelationship;
        
        // Getters and Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getRelationship() { return relationship; }
        public void setRelationship(String relationship) { this.relationship = relationship; }
        
        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        
        public Integer getPercentageShare() { return percentageShare; }
        public void setPercentageShare(Integer percentageShare) { this.percentageShare = percentageShare; }
        
        public String getMinorNomineeFlag() { return minorNomineeFlag; }
        public void setMinorNomineeFlag(String minorNomineeFlag) { this.minorNomineeFlag = minorNomineeFlag; }
        
        public String getGuardianName() { return guardianName; }
        public void setGuardianName(String guardianName) { this.guardianName = guardianName; }
        
        public String getGuardianRelationship() { return guardianRelationship; }
        public void setGuardianRelationship(String guardianRelationship) { this.guardianRelationship = guardianRelationship; }
    }
}