require 'test_helper'

class IndexControllerTest < ActionController::TestCase
  test "should get pags" do
    get :pags
    assert_response :success
  end

end
