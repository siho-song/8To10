
import {TimeSelect, TimeSelectWithPeriod} from "./TimeSelect.jsx";
import * as PropTypes from "prop-types";


InitializeTimeOptions.propTypes = {
    labelText: PropTypes.string.isRequired,
    selectType: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired, // handleChange는 함수 타입
};

export function InitializeTimeOptions({ labelText, selectType, handleChange, errorMessage }) {
    return (
        <div className="form-group">
            <label htmlFor={"schedule-" + selectType + "-time"}>{labelText}</label>
            <TimeSelect selectType={selectType} handleChange={handleChange} />
        </div>
    );
}

InitializeTimeOptionsWithPeriod.propTypes = {
    labelText: PropTypes.string.isRequired,
    selectType: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired, // handleChange는 함수 타입
};

export function InitializeTimeOptionsWithPeriod({ labelText, selectType, handleChange }) {
    return (
        <div className="form-group">
            <label htmlFor={"schedule-"+selectType+"-time"}>{labelText}</label>
            <TimeSelectWithPeriod selectType={selectType} handleChange={handleChange} />
        </div>
    );
}