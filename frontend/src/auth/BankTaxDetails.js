import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import '../styles/BankTaxDetails.css';

const BankTaxDetails = () => {
  const navigate = useNavigate();
  const [userId, setUserId] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [accountNumberError, setAccountNumberError] = useState('');
  const [isChecking, setIsChecking] = useState(false);
  
  const [formData, setFormData] = useState({
    accountType: '',
    accountNumber: '',
    ifscCode: '',
    bankName: '',
    isUSPerson: '',
    taxResidency: 'IND',
    taxId: '', // This will be auto-populated with PAN
  });

  const [errors, setErrors] = useState({});

  // Load userId and fetch PAN from localStorage
  useEffect(() => {
    const savedUserId = localStorage.getItem('userId');
    if (savedUserId) {
      setUserId(savedUserId);
      
      // Fetch PAN from personal details
      try {
        const personalDetails = JSON.parse(localStorage.getItem('personalDetails') || '{}');
        if (personalDetails.pan) {
          setFormData(prev => ({
            ...prev,
            taxId: personalDetails.pan // Auto-populate taxId with PAN
          }));
        }
      } catch (error) {
        console.error('Error loading personal details:', error);
      }
    }
  }, []);

  // Check account number duplicate
  const checkAccountNumberDuplicate = async (accountNumber) => {
    if (!accountNumber || accountNumber.trim().length === 0) return false;
    
    setIsChecking(true);
    try {
      const response = await api.post('/registration/check-duplicates', { accountNumber });
      return response.data.hasErrors && response.data.errors.accountNumber;
    } catch (error) {
      console.error('Error checking account number:', error);
      return false;
    } finally {
      setIsChecking(false);
    }
  };

  const validateTaxId = (value) => {
    const taxIdPattern = /^[A-Z]{5}[0-9]{4}[A-Z]$/;
    return taxIdPattern.test(value);
  };

  const handleAccountTypeChange = (type) => {
    setFormData({ ...formData, accountType: type });
  };

  const handleAccountNumberChange = async (e) => {
    const value = e.target.value.replace(/\D/g, '').slice(0, 30);
    setFormData({ ...formData, accountNumber: value });
    
    // Check account number duplicate
    if (value.length >= 9) {
      const isDuplicate = await checkAccountNumberDuplicate(value);
      setAccountNumberError(isDuplicate ? 'Bank account number already registered' : '');
    } else {
      setAccountNumberError('');
    }
  };

  const handleIfscCodeChange = (e) => {
    let value = e.target.value.toUpperCase().slice(0, 11);
    setFormData({ ...formData, ifscCode: value });
  };

  const handleBankNameChange = (e) => {
    let value = e.target.value.toUpperCase().replace(/[^A-Z\s&]/g, '').slice(0, 50);
    setFormData({ ...formData, bankName: value });
  };

  const handleUSPersonChange = (value) => {
    setFormData({ ...formData, isUSPerson: value });
  };

  const handleTaxResidencyChange = (e) => {
    setFormData({ ...formData, taxResidency: e.target.value });
  };

  const handleTaxIdChange = (e) => {
    let value = e.target.value.toUpperCase().slice(0, 10);
    setFormData({ ...formData, taxId: value });
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.accountType) {
      newErrors.accountType = 'Please select an account type';
    }

    if (formData.accountNumber.length < 3 || formData.accountNumber.length > 30) {
      newErrors.accountNumber = 'Account number must be 3-30 characters';
    }

    if (formData.ifscCode.length < 8 || formData.ifscCode.length > 11) {
      newErrors.ifscCode = 'IFSC code must be 8-11 characters';
    }

    if (!formData.bankName.trim()) {
      newErrors.bankName = 'Bank name is required';
    }

    if (!formData.isUSPerson) {
      newErrors.isUSPerson = 'Please select if you are a US person';
    }

    if (!formData.taxResidency) {
      newErrors.taxResidency = 'Please select tax residency country';
    }

    if (!validateTaxId(formData.taxId)) {
      newErrors.taxId = 'Tax ID must be in format: AAAAA1111A (5 letters, 4 numbers, 1 letter)';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSaveNext = async () => {
    // Check account number duplicate before submission
    const isDuplicate = await checkAccountNumberDuplicate(formData.accountNumber);
    if (isDuplicate) {
      setAccountNumberError('Bank account number already registered');
      return;
    }
    
    if (validateForm()) {
      setIsSubmitting(true);
      try {
        // Save to localStorage
        localStorage.setItem('bankTaxDetails', JSON.stringify(formData));
        
        // Send data to backend
        await api.post(`/registration/bank-tax-details?userId=${userId}`, formData);
        
        // Navigate to scheme selection
        navigate('/scheme-selection');
      } catch (error) {
        console.error('Error saving bank tax details:', error);
        if (error.response && error.response.status === 409) {
          setAccountNumberError(error.response.data);
        } else {
          alert('Error saving bank tax details. Please try again.');
        }
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  const handleBack = () => {
    navigate('/contact-details');
  };

  return (
    <div className="screen3-container">
      <div className="screen3-content">
        <h1 className="screen3-heading">Bank Details-Step 3 of 6</h1>
        

        <div className="form-grid">
          <div className="form-section">
            <label className="form-label">Account Type</label>
            <div className="radio-group">
              <label className="radio-label">
                <input
                  type="radio"
                  name="accountType"
                  value="savings"
                  checked={formData.accountType === 'savings'}
                  onChange={(e) => handleAccountTypeChange(e.target.value)}
                />
                <span>Savings</span>
              </label>
              <label className="radio-label">
                <input
                  type="radio"
                  name="accountType"
                  value="current"
                  checked={formData.accountType === 'current'}
                  onChange={(e) => handleAccountTypeChange(e.target.value)}
                />
                <span>Current</span>
              </label>
            </div>
            {errors.accountType && <p className="error-text">{errors.accountType}</p>}
          </div>

          <div className="form-section">
            <label className="form-label">Account Number</label>
            <input
              type="text"
              className={`form-input ${errors.accountNumber || accountNumberError ? 'error' : ''}`}
              placeholder="Enter account number (3-30 characters)"
              value={formData.accountNumber}
              onChange={handleAccountNumberChange}
              maxLength="30"
            />
            {(errors.accountNumber || accountNumberError) && <p className="error-text">{errors.accountNumber || accountNumberError}</p>}
          </div>

          <div className="form-section">
            <label className="form-label">IFSC Code</label>
            <input
              type="text"
              className={`form-input ${errors.ifscCode ? 'error' : ''}`}
              placeholder="Enter IFSC code (8-11 characters)"
              value={formData.ifscCode}
              onChange={handleIfscCodeChange}
              maxLength="11"
            />
            {errors.ifscCode && <p className="error-text">{errors.ifscCode}</p>}
          </div>

          <div className="form-section">
            <label className="form-label">Bank Name</label>
            <input
              type="text"
              className={`form-input ${errors.bankName ? 'error' : ''}`}
              placeholder="Enter bank name (capital letters only)"
              value={formData.bankName}
              onChange={handleBankNameChange}
              maxLength="50"
            />
            {errors.bankName && <p className="error-text">{errors.bankName}</p>}
          </div>

          <div className="form-section">
            <label className="form-label">Are you a US Person?</label>
            <div className="radio-group">
              <label className="radio-label">
                <input
                  type="radio"
                  name="isUSPerson"
                  value="yes"
                  checked={formData.isUSPerson === 'yes'}
                  onChange={(e) => handleUSPersonChange(e.target.value)}
                />
                <span>Yes</span>
              </label>
              <label className="radio-label">
                <input
                  type="radio"
                  name="isUSPerson"
                  value="no"
                  checked={formData.isUSPerson === 'no'}
                  onChange={(e) => handleUSPersonChange(e.target.value)}
                />
                <span>No</span>
              </label>
            </div>
            {errors.isUSPerson && <p className="error-text">{errors.isUSPerson}</p>}
          </div>

          <div className="form-section">
            <label className="form-label">Country of Tax Residency</label>
            <select
              className={`form-input ${errors.taxResidency ? 'error' : ''}`}
              value={formData.taxResidency}
              onChange={handleTaxResidencyChange}
            >
              <option value="INDIA">INDIA</option>
              <option value="USA">USA</option>
              <option value="UK">UK</option>
              <option value="CANADA">CANADA</option>
              <option value="OTHER">OTHER</option>
            </select>
            {errors.taxResidency && <p className="error-text">{errors.taxResidency}</p>}
          </div>

          <div className="form-section full-width">
            <label className="form-label">Tax ID Number (PAN)</label>
            <div className="tax-id-container">
              <input
                type="text"
                className={`form-input ${errors.taxId ? 'error' : ''} ${formData.taxId ? 'readonly' : ''}`}
                placeholder="Auto-populated from Personal Details"
                value={formData.taxId}
                onChange={handleTaxIdChange}
                maxLength="10"
                readOnly={formData.taxId ? true : false}
              />
              
            </div>
            {errors.taxId && <p className="error-text">{errors.taxId}</p>}
          </div>
        </div>
      </div>

      <div className="button-container">
        <button className="btn btn-back" onClick={handleBack}>
          Back
        </button>
        <button className="btn btn-next" onClick={handleSaveNext} disabled={isSubmitting || isChecking}>
          {isSubmitting ? 'Saving...' : 'Save & Next'}
        </button>
      </div>
    </div>
  );
};

export default BankTaxDetails;
